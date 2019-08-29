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

import React from 'react';
import {SegmentsExperimentType} from '../types.es';

function SegmentsExperimentsDetails({segmentsExperiment}) {
	return (
		<>
			<h4 className="mb-3 mt-4 sheet-subtitle">
				{Liferay.Language.get('details')}
			</h4>

			<dl>
				<div className="d-flex">
					<dt>{Liferay.Language.get('segment') + ':'} </dt>
					<dd className="text-secondary ml-2">
						{segmentsExperiment.segmentsEntryName}
					</dd>
				</div>
				<div className="d-flex">
					<dt>{Liferay.Language.get('goal') + ':'} </dt>
					<dd className="text-secondary ml-2">
						{segmentsExperiment.goal.label}
					</dd>
				</div>
				<div className="d-flex">
					<dt>{Liferay.Language.get('confidence-level') + ':'} </dt>
					<dd className="text-secondary ml-2">
						{segmentsExperiment.confidenceLevel * 100 + '%'}
					</dd>
				</div>
			</dl>
		</>
	);
}

SegmentsExperimentsDetails.propTypes = {
	segmentsExperiment: SegmentsExperimentType
};

export default SegmentsExperimentsDetails;
