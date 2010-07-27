/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of:
 * 
 * a) EPL
 * The Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * b) EDL
 * The Eclipse Development License v1.0 which accompanies this distribution, 
 * is reproduced below, and available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp.newmail;


import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSession;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.ui.SideValue;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;

@SuppressWarnings("restriction")
public class NewMailHandler {
	
	@Execute
	public void execute(
			final MApplication application, IMailSession mailSession) {
		// Create the window
		final MTrimmedWindow window = MBasicFactory.INSTANCE.createTrimmedWindow();
		window.getTags().add("temporaryObject");
		window.setHeight(500);
		window.setWidth(600);
		
		// Create the toolbar
		MTrimBar topTrim = MBasicFactory.INSTANCE.createTrimBar();
		topTrim.setSide(SideValue.TOP);
		window.getTrimBars().add(topTrim);
		
		MToolBar toolbar = MMenuFactory.INSTANCE.createToolBar();
		topTrim.getChildren().add(toolbar);
				
		MHandledToolItem sendItem = MMenuFactory.INSTANCE.createHandledToolItem();
		sendItem.setLabel("Send");
		sendItem.setIconURI("platform:/plugin/org.eclipse.e4.demo.mailapp.newmail/images/email_go.png");
		
		for( MCommand cmd : application.getCommands() ) {
			if( "org.eclipse.e4.demo.mailapp.command.sendmail".equals(cmd.getElementId()) ) {
				sendItem.setCommand(cmd);
			}
		}
		
		toolbar.getChildren().add(sendItem);
				
		// Create the mail editor
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setContributionURI("platform:/plugin/org.eclipse.e4.demo.mailapp.newmail/org.eclipse.e4.demo.mailapp.newmail.EditMailView");
		window.getChildren().add(part);
		application.getChildren().add(window);
		window.getContext().set(IMail.class, mailSession.createMail());
	}
	
	@CanExecute
	public boolean canExecute(@Optional IMailSession mailSession) {
		return mailSession != null;
	}
		
}