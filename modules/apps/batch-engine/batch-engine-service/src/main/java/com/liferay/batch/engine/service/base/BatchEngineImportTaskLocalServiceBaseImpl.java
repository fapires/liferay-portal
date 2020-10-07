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

package com.liferay.batch.engine.service.base;

import com.liferay.batch.engine.model.BatchEngineImportTask;
import com.liferay.batch.engine.model.BatchEngineImportTaskContentBlobModel;
import com.liferay.batch.engine.service.BatchEngineImportTaskLocalService;
import com.liferay.batch.engine.service.persistence.BatchEngineImportTaskPersistence;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.io.AutoDeleteFileInputStream;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.File;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.InputStream;
import java.io.Serializable;

import java.sql.Blob;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the batch engine import task local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.batch.engine.service.impl.BatchEngineImportTaskLocalServiceImpl}.
 * </p>
 *
 * @author Shuyang Zhou
 * @see com.liferay.batch.engine.service.impl.BatchEngineImportTaskLocalServiceImpl
 * @generated
 */
public abstract class BatchEngineImportTaskLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, BatchEngineImportTaskLocalService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>BatchEngineImportTaskLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.batch.engine.service.BatchEngineImportTaskLocalServiceUtil</code>.
	 */

	/**
	 * Adds the batch engine import task to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchEngineImportTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchEngineImportTask the batch engine import task
	 * @return the batch engine import task that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public BatchEngineImportTask addBatchEngineImportTask(
		BatchEngineImportTask batchEngineImportTask) {

		batchEngineImportTask.setNew(true);

		return batchEngineImportTaskPersistence.update(batchEngineImportTask);
	}

	/**
	 * Creates a new batch engine import task with the primary key. Does not add the batch engine import task to the database.
	 *
	 * @param batchEngineImportTaskId the primary key for the new batch engine import task
	 * @return the new batch engine import task
	 */
	@Override
	@Transactional(enabled = false)
	public BatchEngineImportTask createBatchEngineImportTask(
		long batchEngineImportTaskId) {

		return batchEngineImportTaskPersistence.create(batchEngineImportTaskId);
	}

	/**
	 * Deletes the batch engine import task with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchEngineImportTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchEngineImportTaskId the primary key of the batch engine import task
	 * @return the batch engine import task that was removed
	 * @throws PortalException if a batch engine import task with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public BatchEngineImportTask deleteBatchEngineImportTask(
			long batchEngineImportTaskId)
		throws PortalException {

		return batchEngineImportTaskPersistence.remove(batchEngineImportTaskId);
	}

	/**
	 * Deletes the batch engine import task from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchEngineImportTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchEngineImportTask the batch engine import task
	 * @return the batch engine import task that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public BatchEngineImportTask deleteBatchEngineImportTask(
		BatchEngineImportTask batchEngineImportTask) {

		return batchEngineImportTaskPersistence.remove(batchEngineImportTask);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return batchEngineImportTaskPersistence.dslQuery(dslQuery);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			BatchEngineImportTask.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return batchEngineImportTaskPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.engine.model.impl.BatchEngineImportTaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return batchEngineImportTaskPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.engine.model.impl.BatchEngineImportTaskModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return batchEngineImportTaskPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return batchEngineImportTaskPersistence.countWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return batchEngineImportTaskPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public BatchEngineImportTask fetchBatchEngineImportTask(
		long batchEngineImportTaskId) {

		return batchEngineImportTaskPersistence.fetchByPrimaryKey(
			batchEngineImportTaskId);
	}

	/**
	 * Returns the batch engine import task with the matching UUID and company.
	 *
	 * @param uuid the batch engine import task's UUID
	 * @param companyId the primary key of the company
	 * @return the matching batch engine import task, or <code>null</code> if a matching batch engine import task could not be found
	 */
	@Override
	public BatchEngineImportTask fetchBatchEngineImportTaskByUuidAndCompanyId(
		String uuid, long companyId) {

		return batchEngineImportTaskPersistence.fetchByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns the batch engine import task with the primary key.
	 *
	 * @param batchEngineImportTaskId the primary key of the batch engine import task
	 * @return the batch engine import task
	 * @throws PortalException if a batch engine import task with the primary key could not be found
	 */
	@Override
	public BatchEngineImportTask getBatchEngineImportTask(
			long batchEngineImportTaskId)
		throws PortalException {

		return batchEngineImportTaskPersistence.findByPrimaryKey(
			batchEngineImportTaskId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			batchEngineImportTaskLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(BatchEngineImportTask.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"batchEngineImportTaskId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			batchEngineImportTaskLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			BatchEngineImportTask.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"batchEngineImportTaskId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			batchEngineImportTaskLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(BatchEngineImportTask.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"batchEngineImportTaskId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<BatchEngineImportTask>() {

				@Override
				public void performAction(
						BatchEngineImportTask batchEngineImportTask)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, batchEngineImportTask);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(
					BatchEngineImportTask.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return batchEngineImportTaskPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return batchEngineImportTaskLocalService.deleteBatchEngineImportTask(
			(BatchEngineImportTask)persistedModel);
	}

	@Override
	public BasePersistence<BatchEngineImportTask> getBasePersistence() {
		return batchEngineImportTaskPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return batchEngineImportTaskPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns the batch engine import task with the matching UUID and company.
	 *
	 * @param uuid the batch engine import task's UUID
	 * @param companyId the primary key of the company
	 * @return the matching batch engine import task
	 * @throws PortalException if a matching batch engine import task could not be found
	 */
	@Override
	public BatchEngineImportTask getBatchEngineImportTaskByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return batchEngineImportTaskPersistence.findByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns a range of all the batch engine import tasks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.batch.engine.model.impl.BatchEngineImportTaskModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of batch engine import tasks
	 * @param end the upper bound of the range of batch engine import tasks (not inclusive)
	 * @return the range of batch engine import tasks
	 */
	@Override
	public List<BatchEngineImportTask> getBatchEngineImportTasks(
		int start, int end) {

		return batchEngineImportTaskPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of batch engine import tasks.
	 *
	 * @return the number of batch engine import tasks
	 */
	@Override
	public int getBatchEngineImportTasksCount() {
		return batchEngineImportTaskPersistence.countAll();
	}

	/**
	 * Updates the batch engine import task in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect BatchEngineImportTaskLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param batchEngineImportTask the batch engine import task
	 * @return the batch engine import task that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public BatchEngineImportTask updateBatchEngineImportTask(
		BatchEngineImportTask batchEngineImportTask) {

		return batchEngineImportTaskPersistence.update(batchEngineImportTask);
	}

	@Override
	public BatchEngineImportTaskContentBlobModel getContentBlobModel(
		Serializable primaryKey) {

		Session session = null;

		try {
			session = batchEngineImportTaskPersistence.openSession();

			return (BatchEngineImportTaskContentBlobModel)session.get(
				BatchEngineImportTaskContentBlobModel.class, primaryKey);
		}
		catch (Exception exception) {
			throw batchEngineImportTaskPersistence.processException(exception);
		}
		finally {
			batchEngineImportTaskPersistence.closeSession(session);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public InputStream openContentInputStream(long batchEngineImportTaskId) {
		try {
			BatchEngineImportTaskContentBlobModel
				BatchEngineImportTaskContentBlobModel = getContentBlobModel(
					batchEngineImportTaskId);

			Blob blob = BatchEngineImportTaskContentBlobModel.getContentBlob();

			if (blob == null) {
				return _EMPTY_INPUT_STREAM;
			}

			InputStream inputStream = blob.getBinaryStream();

			if (_useTempFile) {
				inputStream = new AutoDeleteFileInputStream(
					_file.createTempFile(inputStream));
			}

			return inputStream;
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	@Activate
	protected void activate() {
		DB db = DBManagerUtil.getDB();

		if ((db.getDBType() != DBType.DB2) &&
			(db.getDBType() != DBType.MYSQL) &&
			(db.getDBType() != DBType.MARIADB) &&
			(db.getDBType() != DBType.SYBASE)) {

			_useTempFile = true;
		}
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			BatchEngineImportTaskLocalService.class,
			IdentifiableOSGiService.class, PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		batchEngineImportTaskLocalService =
			(BatchEngineImportTaskLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return BatchEngineImportTaskLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return BatchEngineImportTask.class;
	}

	protected String getModelClassName() {
		return BatchEngineImportTask.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				batchEngineImportTaskPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	protected BatchEngineImportTaskLocalService
		batchEngineImportTaskLocalService;

	@Reference
	protected BatchEngineImportTaskPersistence batchEngineImportTaskPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected File _file;

	private static final InputStream _EMPTY_INPUT_STREAM =
		new UnsyncByteArrayInputStream(new byte[0]);

	private boolean _useTempFile;

}