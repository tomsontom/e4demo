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


import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

@SuppressWarnings("restriction")
public class NewMailHandler {
	
	@Execute
	public void execute(
			final MApplication application) {
		final MTrimmedWindow window = MBasicFactory.INSTANCE.createTrimmedWindow();
		window.getTags().add("newMailWindow");
		window.setHeight(500);
		window.setWidth(600);
		window.setToBeRendered(true);
		window.setVisible(true);
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setContributionURI("platform:/plugin/org.eclipse.e4.demo.mailapp.newmail/org.eclipse.e4.demo.mailapp.newmail.EditMailView");
		window.getChildren().add(part);
		application.getChildren().add(window);
		//TODO Find better solution
		Widget s = (Widget) window.getWidget();
		s.addDisposeListener(new DisposeListener() {
			
			public void widgetDisposed(DisposeEvent e) {
				application.getChildren().remove(window);
			}
		});
	}
	
	@CanExecute
	public boolean canExecute() {
		//TODO Your code goes here
		return true;
	}
		
}