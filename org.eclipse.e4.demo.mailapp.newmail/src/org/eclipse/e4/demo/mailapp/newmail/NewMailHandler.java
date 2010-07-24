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
package org.eclipse.e4.demo.mailapp.newmail;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolderContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class NewMailHandler {
	
	@Execute
	public void execute(
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, 
			@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IFolderContainer selection) {
		System.err.println("Execute");
//		MTrimmedWindow window = MBasicFactory.INSTANCE.createTrimmedWindow();
//		window.setToBeRendered(true);
//		window.setVisible(true);
//		MPart part = MBasicFactory.INSTANCE.createPart();
//		part.setContributionURI("platform:/plugin/org.eclipse.e4.demo.mailapp.newmail/org.eclipse.e4.demo.mailapp.newmail.EditMailView");
//		window.getChildren().add(part);
	}
	
	@CanExecute
	public boolean canExecute() {
		//TODO Your code goes here
		return true;
	}
		
}