/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.configuration.settings.internal;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.configuration.settings.internal.scoped.configuration.ScopedConfigurationManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactory;
import com.liferay.portal.kernel.resource.manager.ClassLoaderResourceManager;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.settings.ConfigurationBeanSettings;
import com.liferay.portal.kernel.settings.LocationVariableResolver;
import com.liferay.portal.kernel.settings.PortletPreferencesSettings;
import com.liferay.portal.kernel.settings.PropertiesSettings;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsLocatorHelper;
import com.liferay.portal.kernel.settings.definition.ConfigurationBeanDeclaration;
import com.liferay.portal.kernel.settings.definition.ConfigurationPidMapping;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.util.PrefsPropsUtil;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.portlet.PortletPreferences;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Iván Zaera
 * @author Jorge Ferrer
 * @author Shuyang Zhou
 */
@Component(immediate = true, service = SettingsLocatorHelper.class)
public class SettingsLocatorHelperImpl implements SettingsLocatorHelper {

	@Override
	public Settings getCompanyConfigurationBeanSettings(
		long companyId, String configurationPid, Settings parentSettings) {

		return _getScopedConfigurationBeanSettings(
			ExtendedObjectClassDefinition.Scope.COMPANY, companyId,
			configurationPid, parentSettings);
	}

	public PortletPreferences getCompanyPortletPreferences(
		long companyId, String settingsId) {

		return _portletPreferencesLocalService.getStrictPreferences(
			companyId, companyId, PortletKeys.PREFS_OWNER_TYPE_COMPANY, 0,
			settingsId);
	}

	@Override
	public Settings getCompanyPortletPreferencesSettings(
		long companyId, String settingsId, Settings parentSettings) {

		return new PortletPreferencesSettings(
			getCompanyPortletPreferences(companyId, settingsId),
			parentSettings);
	}

	@Override
	public Settings getConfigurationBeanSettings(String configurationPid) {
		Class<?> configurationBeanClass = _configurationBeanClasses.get(
			configurationPid);

		if (configurationBeanClass == null) {
			return _portalPropertiesSettings;
		}

		Settings configurationBeanSettings = _configurationBeanSettings.get(
			configurationBeanClass);

		if (configurationBeanSettings == null) {
			return _portalPropertiesSettings;
		}

		return configurationBeanSettings;
	}

	@Override
	public Settings getGroupConfigurationBeanSettings(
		long groupId, String configurationPid, Settings parentSettings) {

		return _getScopedConfigurationBeanSettings(
			ExtendedObjectClassDefinition.Scope.GROUP, groupId,
			configurationPid, parentSettings);
	}

	public PortletPreferences getGroupPortletPreferences(
		long groupId, String settingsId) {

		try {
			Group group = _groupLocalService.getGroup(groupId);

			return _portletPreferencesLocalService.getStrictPreferences(
				group.getCompanyId(), groupId,
				PortletKeys.PREFS_OWNER_TYPE_GROUP, 0, settingsId);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	@Override
	public Settings getGroupPortletPreferencesSettings(
		long groupId, String settingsId, Settings parentSettings) {

		return new PortletPreferencesSettings(
			getGroupPortletPreferences(groupId, settingsId), parentSettings);
	}

	@Override
	public Settings getPortalPreferencesSettings(
		long companyId, Settings parentSettings) {

		return new PortletPreferencesSettings(
			PrefsPropsUtil.getPreferences(companyId), parentSettings);
	}

	@Override
	public Settings getPortletInstanceConfigurationBeanSettings(
		String portletId, String configurationPid, Settings parentSettings) {

		return _getScopedConfigurationBeanSettings(
			ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE, portletId,
			configurationPid, parentSettings);
	}

	public PortletPreferences getPortletInstancePortletPreferences(
		long companyId, long ownerId, int ownerType, long plid,
		String portletId) {

		if (plid != LayoutConstants.DEFAULT_PLID) {
			Layout layout = _layoutLocalService.fetchLayout(plid);

			if (layout != null) {
				return _portletPreferencesFactory.getStrictPortletSetup(
					layout, portletId);
			}
		}

		if (PortletIdCodec.hasUserId(portletId)) {
			ownerId = PortletIdCodec.decodeUserId(portletId);
			ownerType = PortletKeys.PREFS_OWNER_TYPE_USER;
		}

		return _portletPreferencesLocalService.getStrictPreferences(
			companyId, ownerId, ownerType, plid, portletId);
	}

	public PortletPreferences getPortletInstancePortletPreferences(
		long companyId, long plid, String portletId) {

		return getPortletInstancePortletPreferences(
			companyId, PortletKeys.PREFS_OWNER_ID_DEFAULT,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT, plid, portletId);
	}

	@Override
	public Settings getPortletInstancePortletPreferencesSettings(
		long companyId, long ownerId, int ownerType, long plid,
		String portletId, Settings parentSettings) {

		return new PortletPreferencesSettings(
			getPortletInstancePortletPreferences(
				companyId, ownerId, ownerType, plid, portletId),
			parentSettings);
	}

	@Override
	public Settings getPortletInstancePortletPreferencesSettings(
		long companyId, long plid, String portletId, Settings parentSettings) {

		return new PortletPreferencesSettings(
			getPortletInstancePortletPreferences(companyId, plid, portletId),
			parentSettings);
	}

	@Override
	public Settings getServerSettings(String settingsId) {
		return getConfigurationBeanSettings(settingsId);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_configurationBeanDeclarationServiceTracker =
			new ConfigurationBeanDeclarationServiceTracker(bundleContext);

		_configurationBeanDeclarationServiceTracker.open();

		_configurationBeanDeclarationServiceTrackerFactory =
			new ConfigurationBeanDeclarationServiceTrackerFactory(
				bundleContext);

		_configurationBeanDeclarationServiceTrackerFactory.open();
	}

	@Deactivate
	protected void deactivate() {
		_configurationBeanDeclarationServiceTracker.close();
		_configurationBeanDeclarationServiceTrackerFactory.close();
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void setConfigurationPidMapping(
		ConfigurationPidMapping configurationPidMapping) {

		_configurationBeanClasses.put(
			configurationPidMapping.getConfigurationPid(),
			configurationPidMapping.getConfigurationBeanClass());
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	@Reference(unbind = "-")
	protected void setLayoutLocalService(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
	}

	@Reference(unbind = "-")
	protected void setPortletLocalService(
		PortletLocalService portletLocalService) {
	}

	@Reference(unbind = "-")
	protected void setPortletPreferencesFactory(
		PortletPreferencesFactory portletPreferencesFactory) {

		_portletPreferencesFactory = portletPreferencesFactory;
	}

	@Reference(unbind = "-")
	protected void setPortletPreferencesLocalService(
		PortletPreferencesLocalService portletPreferencesLocalService) {

		_portletPreferencesLocalService = portletPreferencesLocalService;
	}

	@Reference(unbind = "-")
	protected void setProps(Props props) {
		_portalPropertiesSettings = new PropertiesSettings(
			new LocationVariableResolver(
				new ClassLoaderResourceManager(
					PortalClassLoaderUtil.getClassLoader()),
				this),
			props.getProperties());
	}

	protected void unsetConfigurationPidMapping(
		ConfigurationPidMapping configurationPidMapping) {

		_configurationBeanClasses.remove(
			configurationPidMapping.getConfigurationPid());
	}

	private Settings _getScopedConfigurationBeanSettings(
		ExtendedObjectClassDefinition.Scope scope, Serializable scopePK,
		String configurationPid, Settings parentSettings) {

		ScopedConfigurationManager scopedConfigurationManager =
			_scopedConfigurationManagers.get(configurationPid);

		if (scopedConfigurationManager == null) {
			return parentSettings;
		}

		Object configurationBean = scopedConfigurationManager.getConfiguration(
			scope, scopePK);

		if (configurationBean == null) {
			return parentSettings;
		}

		return new ConfigurationBeanSettings(
			scopedConfigurationManager.getLocationVariableResolver(),
			configurationBean, parentSettings);
	}

	private final ConcurrentMap<String, Class<?>> _configurationBeanClasses =
		new ConcurrentHashMap<>();
	private ServiceTracker
		<ConfigurationBeanDeclaration, ConfigurationBeanManagedService>
			_configurationBeanDeclarationServiceTracker;
	private ServiceTracker
		<ConfigurationBeanDeclaration, ScopedConfigurationManager>
			_configurationBeanDeclarationServiceTrackerFactory;
	private final Map<Class<?>, Settings> _configurationBeanSettings =
		new ConcurrentHashMap<>();
	private GroupLocalService _groupLocalService;
	private LayoutLocalService _layoutLocalService;
	private Settings _portalPropertiesSettings;
	private PortletPreferencesFactory _portletPreferencesFactory;
	private PortletPreferencesLocalService _portletPreferencesLocalService;
	private final Map<String, ScopedConfigurationManager>
		_scopedConfigurationManagers = new ConcurrentHashMap<>();

	private class ConfigurationBeanDeclarationServiceTracker
		extends ServiceTracker
			<ConfigurationBeanDeclaration, ConfigurationBeanManagedService> {

		@Override
		public ConfigurationBeanManagedService addingService(
			ServiceReference<ConfigurationBeanDeclaration> serviceReference) {

			ConfigurationBeanDeclaration configurationBeanDeclaration =
				context.getService(serviceReference);

			Class<?> configurationBeanClass =
				configurationBeanDeclaration.getConfigurationBeanClass();

			ConfigurationBeanManagedService configurationBeanManagedService =
				new ConfigurationBeanManagedService(
					context, configurationBeanClass,
					configurationBean -> {
						LocationVariableResolver locationVariableResolver =
							new LocationVariableResolver(
								new ClassLoaderResourceManager(
									configurationBeanClass.getClassLoader()),
								SettingsLocatorHelperImpl.this);

						_configurationBeanSettings.put(
							configurationBeanClass,
							new ConfigurationBeanSettings(
								locationVariableResolver, configurationBean,
								_portalPropertiesSettings));
					});

			_configurationBeanClasses.put(
				configurationBeanManagedService.getConfigurationPid(),
				configurationBeanClass);

			configurationBeanManagedService.register();

			return configurationBeanManagedService;
		}

		@Override
		public void removedService(
			ServiceReference<ConfigurationBeanDeclaration> serviceReference,
			ConfigurationBeanManagedService configurationBeanManagedService) {

			context.ungetService(serviceReference);

			configurationBeanManagedService.unregister();

			Class<?> configurationBeanClass = _configurationBeanClasses.remove(
				configurationBeanManagedService.getConfigurationPid());

			_configurationBeanSettings.remove(configurationBeanClass);
		}

		private ConfigurationBeanDeclarationServiceTracker(
			BundleContext bundleContext) {

			super(bundleContext, ConfigurationBeanDeclaration.class, null);
		}

	}

	private class ConfigurationBeanDeclarationServiceTrackerFactory
		extends ServiceTracker
			<ConfigurationBeanDeclaration, ScopedConfigurationManager> {

		@Override
		public ScopedConfigurationManager addingService(
			ServiceReference<ConfigurationBeanDeclaration> reference) {

			ConfigurationBeanDeclaration configurationBeanDeclaration =
				context.getService(reference);

			Class<?> configurationBeanClass =
				configurationBeanDeclaration.getConfigurationBeanClass();

			LocationVariableResolver locationVariableResolver =
				new LocationVariableResolver(
					new ClassLoaderResourceManager(
						configurationBeanClass.getClassLoader()),
					SettingsLocatorHelperImpl.this);

			ScopedConfigurationManager scopedConfigurationManager =
				new ScopedConfigurationManager(
					context, configurationBeanClass, locationVariableResolver);

			scopedConfigurationManager.register();

			_scopedConfigurationManagers.put(
				scopedConfigurationManager.getName(),
				scopedConfigurationManager);

			return scopedConfigurationManager;
		}

		@Override
		public void removedService(
			ServiceReference<ConfigurationBeanDeclaration> reference,
			ScopedConfigurationManager scopedConfigurationManager) {

			context.ungetService(reference);

			_scopedConfigurationManagers.remove(
				scopedConfigurationManager.getName());

			scopedConfigurationManager.unregister();
		}

		private ConfigurationBeanDeclarationServiceTrackerFactory(
			BundleContext bundleContext) {

			super(bundleContext, ConfigurationBeanDeclaration.class, null);
		}

	}

}