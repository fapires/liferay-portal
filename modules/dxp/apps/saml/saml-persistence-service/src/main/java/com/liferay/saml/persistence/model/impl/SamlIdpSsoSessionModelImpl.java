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

package com.liferay.saml.persistence.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;

import com.liferay.saml.persistence.model.SamlIdpSsoSession;
import com.liferay.saml.persistence.model.SamlIdpSsoSessionModel;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the SamlIdpSsoSession service. Represents a row in the &quot;SamlIdpSsoSession&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link SamlIdpSsoSessionModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SamlIdpSsoSessionImpl}.
 * </p>
 *
 * @author Mika Koivisto
 * @see SamlIdpSsoSessionImpl
 * @see SamlIdpSsoSession
 * @see SamlIdpSsoSessionModel
 * @generated
 */
@ProviderType
public class SamlIdpSsoSessionModelImpl extends BaseModelImpl<SamlIdpSsoSession>
	implements SamlIdpSsoSessionModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a saml idp sso session model instance should use the {@link SamlIdpSsoSession} interface instead.
	 */
	public static final String TABLE_NAME = "SamlIdpSsoSession";
	public static final Object[][] TABLE_COLUMNS = {
			{ "samlIdpSsoSessionId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "samlIdpSsoSessionKey", Types.VARCHAR }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("samlIdpSsoSessionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("samlIdpSsoSessionKey", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE = "create table SamlIdpSsoSession (samlIdpSsoSessionId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,samlIdpSsoSessionKey VARCHAR(75) null)";
	public static final String TABLE_SQL_DROP = "drop table SamlIdpSsoSession";
	public static final String ORDER_BY_JPQL = " ORDER BY samlIdpSsoSession.samlIdpSsoSessionId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY SamlIdpSsoSession.samlIdpSsoSessionId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.saml.persistence.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.saml.persistence.model.SamlIdpSsoSession"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.saml.persistence.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.saml.persistence.model.SamlIdpSsoSession"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.saml.persistence.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.saml.persistence.model.SamlIdpSsoSession"),
			true);
	public static final long CREATEDATE_COLUMN_BITMASK = 1L;
	public static final long SAMLIDPSSOSESSIONKEY_COLUMN_BITMASK = 2L;
	public static final long SAMLIDPSSOSESSIONID_COLUMN_BITMASK = 4L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.saml.persistence.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.saml.persistence.model.SamlIdpSsoSession"));

	public SamlIdpSsoSessionModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _samlIdpSsoSessionId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setSamlIdpSsoSessionId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _samlIdpSsoSessionId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SamlIdpSsoSession.class;
	}

	@Override
	public String getModelClassName() {
		return SamlIdpSsoSession.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("samlIdpSsoSessionId", getSamlIdpSsoSessionId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("samlIdpSsoSessionKey", getSamlIdpSsoSessionKey());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long samlIdpSsoSessionId = (Long)attributes.get("samlIdpSsoSessionId");

		if (samlIdpSsoSessionId != null) {
			setSamlIdpSsoSessionId(samlIdpSsoSessionId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String samlIdpSsoSessionKey = (String)attributes.get(
				"samlIdpSsoSessionKey");

		if (samlIdpSsoSessionKey != null) {
			setSamlIdpSsoSessionKey(samlIdpSsoSessionKey);
		}
	}

	@Override
	public long getSamlIdpSsoSessionId() {
		return _samlIdpSsoSessionId;
	}

	@Override
	public void setSamlIdpSsoSessionId(long samlIdpSsoSessionId) {
		_samlIdpSsoSessionId = samlIdpSsoSessionId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_columnBitmask |= CREATEDATE_COLUMN_BITMASK;

		if (_originalCreateDate == null) {
			_originalCreateDate = _createDate;
		}

		_createDate = createDate;
	}

	public Date getOriginalCreateDate() {
		return _originalCreateDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@Override
	public String getSamlIdpSsoSessionKey() {
		if (_samlIdpSsoSessionKey == null) {
			return "";
		}
		else {
			return _samlIdpSsoSessionKey;
		}
	}

	@Override
	public void setSamlIdpSsoSessionKey(String samlIdpSsoSessionKey) {
		_columnBitmask |= SAMLIDPSSOSESSIONKEY_COLUMN_BITMASK;

		if (_originalSamlIdpSsoSessionKey == null) {
			_originalSamlIdpSsoSessionKey = _samlIdpSsoSessionKey;
		}

		_samlIdpSsoSessionKey = samlIdpSsoSessionKey;
	}

	public String getOriginalSamlIdpSsoSessionKey() {
		return GetterUtil.getString(_originalSamlIdpSsoSessionKey);
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			SamlIdpSsoSession.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SamlIdpSsoSession toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (SamlIdpSsoSession)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SamlIdpSsoSessionImpl samlIdpSsoSessionImpl = new SamlIdpSsoSessionImpl();

		samlIdpSsoSessionImpl.setSamlIdpSsoSessionId(getSamlIdpSsoSessionId());
		samlIdpSsoSessionImpl.setCompanyId(getCompanyId());
		samlIdpSsoSessionImpl.setUserId(getUserId());
		samlIdpSsoSessionImpl.setUserName(getUserName());
		samlIdpSsoSessionImpl.setCreateDate(getCreateDate());
		samlIdpSsoSessionImpl.setModifiedDate(getModifiedDate());
		samlIdpSsoSessionImpl.setSamlIdpSsoSessionKey(getSamlIdpSsoSessionKey());

		samlIdpSsoSessionImpl.resetOriginalValues();

		return samlIdpSsoSessionImpl;
	}

	@Override
	public int compareTo(SamlIdpSsoSession samlIdpSsoSession) {
		long primaryKey = samlIdpSsoSession.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SamlIdpSsoSession)) {
			return false;
		}

		SamlIdpSsoSession samlIdpSsoSession = (SamlIdpSsoSession)obj;

		long primaryKey = samlIdpSsoSession.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		SamlIdpSsoSessionModelImpl samlIdpSsoSessionModelImpl = this;

		samlIdpSsoSessionModelImpl._originalCreateDate = samlIdpSsoSessionModelImpl._createDate;

		samlIdpSsoSessionModelImpl._setModifiedDate = false;

		samlIdpSsoSessionModelImpl._originalSamlIdpSsoSessionKey = samlIdpSsoSessionModelImpl._samlIdpSsoSessionKey;

		samlIdpSsoSessionModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<SamlIdpSsoSession> toCacheModel() {
		SamlIdpSsoSessionCacheModel samlIdpSsoSessionCacheModel = new SamlIdpSsoSessionCacheModel();

		samlIdpSsoSessionCacheModel.samlIdpSsoSessionId = getSamlIdpSsoSessionId();

		samlIdpSsoSessionCacheModel.companyId = getCompanyId();

		samlIdpSsoSessionCacheModel.userId = getUserId();

		samlIdpSsoSessionCacheModel.userName = getUserName();

		String userName = samlIdpSsoSessionCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			samlIdpSsoSessionCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			samlIdpSsoSessionCacheModel.createDate = createDate.getTime();
		}
		else {
			samlIdpSsoSessionCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			samlIdpSsoSessionCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			samlIdpSsoSessionCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		samlIdpSsoSessionCacheModel.samlIdpSsoSessionKey = getSamlIdpSsoSessionKey();

		String samlIdpSsoSessionKey = samlIdpSsoSessionCacheModel.samlIdpSsoSessionKey;

		if ((samlIdpSsoSessionKey != null) &&
				(samlIdpSsoSessionKey.length() == 0)) {
			samlIdpSsoSessionCacheModel.samlIdpSsoSessionKey = null;
		}

		return samlIdpSsoSessionCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(15);

		sb.append("{samlIdpSsoSessionId=");
		sb.append(getSamlIdpSsoSessionId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", samlIdpSsoSessionKey=");
		sb.append(getSamlIdpSsoSessionKey());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(25);

		sb.append("<model><model-name>");
		sb.append("com.liferay.saml.persistence.model.SamlIdpSsoSession");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>samlIdpSsoSessionId</column-name><column-value><![CDATA[");
		sb.append(getSamlIdpSsoSessionId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>samlIdpSsoSessionKey</column-name><column-value><![CDATA[");
		sb.append(getSamlIdpSsoSessionKey());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = SamlIdpSsoSession.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			SamlIdpSsoSession.class, ModelWrapper.class
		};
	private long _samlIdpSsoSessionId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _originalCreateDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _samlIdpSsoSessionKey;
	private String _originalSamlIdpSsoSessionKey;
	private long _columnBitmask;
	private SamlIdpSsoSession _escapedModel;
}