/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp;

import java.util.Collections;
import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

@SuppressWarnings("restriction")
public class LifecycleHandler {

	@PreSave
	void cleanupModel(MApplication application, EModelService modelService) {
		List<Object> list = modelService.findElements(application, null, null,
				Collections.singletonList("temporaryObject"));
		for (Object o : list) {
			EcoreUtil.delete((EObject) o, true);
		}
	}
}
