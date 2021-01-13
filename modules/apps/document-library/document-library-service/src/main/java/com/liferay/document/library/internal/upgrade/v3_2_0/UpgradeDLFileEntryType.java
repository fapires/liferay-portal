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

package com.liferay.document.library.internal.upgrade.v3_2_0;

import com.liferay.document.library.kernel.model.DLFileEntryMetadata;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Adolfo Pérez
 * @author Alejandro Tardín
 */
public class UpgradeDLFileEntryType
	extends com.liferay.portal.upgrade.v7_3_x.UpgradeDLFileEntryType {

	@Override
	protected void doUpgrade() throws Exception {
		super.doUpgrade();

		try (PreparedStatement ps1 = connection.prepareStatement(
				StringBundler.concat(
					"select userId, userName, companyId, groupId, ",
					"fileEntryTypeId, fileEntryTypeKey, name from ",
					"DLFileEntryType where (dataDefinitionId is null or ",
					"dataDefinitionId = 0) and fileEntryTypeKey != ",
					"'BASIC-DOCUMENT'"));
			PreparedStatement ps2 = AutoBatchPreparedStatementUtil.autoBatch(
				connection.prepareStatement(
					"update DLFileEntryType set dataDefinitionId = ? where " +
						"fileEntryTypeId = ? "));
			ResultSet rs = ps1.executeQuery()) {

			String ddmStructureDefinition = StringUtil.read(
				UpgradeDLFileEntryType.class.getResourceAsStream(
					"dependencies/ddm-structure.json"));

			String ddmStructureLayoutDefinition = StringUtil.read(
				UpgradeDLFileEntryType.class.getResourceAsStream(
					"dependencies/ddm-structure-layout.json"));

			while (rs.next()) {
				long ddmStructureId = _insertDDMStructure(
					rs.getLong("groupId"), rs.getLong("companyId"),
					rs.getLong("userId"), rs.getString("userName"),
					rs.getString("name"), ddmStructureDefinition);

				long ddmStructureVersionId = _insertDDMStructureVersion(
					rs.getLong("userId"), rs.getString("userName"),
					rs.getLong("companyId"), rs.getLong("groupId"),
					ddmStructureId, rs.getString("name"),
					ddmStructureDefinition);

				_insertDDMStructureLayout(
					rs.getLong("userId"), rs.getString("userName"),
					rs.getLong("groupId"), rs.getLong("companyId"),
					ddmStructureId, rs.getString("name"), ddmStructureVersionId,
					ddmStructureLayoutDefinition);

				ResourceLocalServiceUtil.addResources(
					rs.getLong("companyId"), rs.getLong("groupId"),
					rs.getLong("userId"),
					ResourceActionsUtil.getCompositeModelName(
						DLFileEntryMetadata.class.getName(),
						DDMStructure.class.getName()),
					ddmStructureId, false, false, false);

				ps2.setLong(1, ddmStructureId);

				ps2.setLong(2, rs.getLong("fileEntryTypeId"));

				ps2.addBatch();
			}

			ps2.executeBatch();
		}
	}

	private long _insertDDMStructure(
			long groupId, long companyId, long userId, String userName,
			String name, String ddmStructureDefinition)
		throws Exception {

		try (PreparedStatement ps2 = connection.prepareStatement(
				StringBundler.concat(
					"insert into DDMStructure (mvccVersion, ctCollectionId, ",
					"uuid_, structureId, groupId, companyId, userId, ",
					"userName, versionUserId, versionUserName, createDate, ",
					"modifiedDate, parentStructureId, classNameId, ",
					"structureKey, version, name, description, definition, ",
					"storageType, type_, lastPublishDate) values (0, 0, ?, ?, ",
					"?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, '1.0', ?, '', ?, ",
					"'default', 1, null)"))) {

			long classNameId = PortalUtil.getClassNameId(
				DLFileEntryMetadata.class);

			ps2.setString(1, PortalUUIDUtil.generate());

			long ddmStructureId = increment();

			ps2.setLong(2, ddmStructureId);

			ps2.setLong(3, groupId);
			ps2.setLong(4, companyId);
			ps2.setLong(5, userId);
			ps2.setString(6, userName);
			ps2.setLong(7, userId);
			ps2.setString(8, userName);

			Date now = new Date(System.currentTimeMillis());

			ps2.setDate(9, now);
			ps2.setDate(10, now);

			ps2.setLong(11, classNameId);

			ps2.setString(12, String.valueOf(ddmStructureId));
			ps2.setString(13, name);
			ps2.setString(14, ddmStructureDefinition);

			ps2.executeUpdate();

			return ddmStructureId;
		}
	}

	private void _insertDDMStructureLayout(
			long userId, String userName, long groupId, long companyId,
			long ddmStructureId, String name, long ddmStructureVersionId,
			String ddmStructureLayoutDefinition)
		throws Exception {

		try (PreparedStatement ps = connection.prepareStatement(
				StringBundler.concat(
					"insert into DDMStructureLayout (mvccVersion, ",
					"ctCollectionId, uuid_, structureLayoutId, groupId, ",
					"companyId, userId, userName, createDate, modifiedDate, ",
					"classNameId, structureLayoutKey, structureVersionId, ",
					"name, description, definition) values (0, 0, ?, ?, ?, ?, ",
					"?, ?, ?, ?, ?, ?, ?, ?, '', ?)"))) {

			long classNameId = PortalUtil.getClassNameId(
				DLFileEntryMetadata.class);

			ps.setString(1, PortalUUIDUtil.generate());
			ps.setLong(2, increment());
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, userId);
			ps.setString(6, userName);

			Date now = new Date(System.currentTimeMillis());

			ps.setDate(7, now);
			ps.setDate(8, now);

			ps.setLong(9, classNameId);

			ps.setString(10, String.valueOf(ddmStructureId));
			ps.setLong(11, ddmStructureVersionId);
			ps.setString(12, name);
			ps.setString(13, ddmStructureLayoutDefinition);

			ps.executeUpdate();
		}
	}

	private long _insertDDMStructureVersion(
			long userId, String userName, long companyId, long groupId,
			long ddmStructureId, String name, String ddmStructureDefinition)
		throws Exception {

		try (PreparedStatement ps = connection.prepareStatement(
				StringBundler.concat(
					"insert into DDMStructureVersion (mvccVersion, ",
					"ctCollectionId, structureVersionId, groupId, companyId, ",
					"userId, userName, createDate, structureId, version, ",
					"parentStructureId, name, description, definition, ",
					"storageType, type_, status, statusByUserId, ",
					"statusByUserName, statusDate) values (0, 0, ?, ?, ?, ?, ",
					"?, ?, ?, '1.0', 0, ?, '', ? , 'default', 0, 0, ?, ?, ",
					"?)"))) {

			long structureVersionId = increment();

			ps.setLong(1, structureVersionId);

			ps.setLong(2, groupId);
			ps.setLong(3, companyId);
			ps.setLong(4, userId);
			ps.setString(5, userName);

			Date now = new Date(System.currentTimeMillis());

			ps.setDate(6, now);

			ps.setLong(7, ddmStructureId);
			ps.setString(8, name);
			ps.setString(9, ddmStructureDefinition);

			ps.setLong(10, userId);
			ps.setString(11, userName);
			ps.setDate(12, now);

			ps.executeUpdate();

			return structureVersionId;
		}
	}

}