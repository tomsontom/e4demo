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
package org.eclipse.e4.demo.mailapp;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

@SuppressWarnings("restriction")
public class PreferenceDialog extends TitleAreaDialog {
	
	@Inject
	@Preference("username")
	private String username;
	
	@Inject
	@Preference("password")
	private String password;
	
	@Inject
	@Preference("host")
	private String host; 
	
	private Text usernameField;
	private Text passwordField;
	private Text hostField;
	
	@Inject
	public PreferenceDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		
		getShell().setText("Connection informations");
		setTitle("Connection informations");
		setMessage("Configure the connection informations");
		
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout(2, false));
		
		Label l = new Label(container, SWT.NONE);
		l.setText("Username");
		
		usernameField = new Text(container, SWT.BORDER);
		usernameField.setText(username == null ? "" : username);
		usernameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		l = new Label(container, SWT.NONE);
		l.setText("Password");
		
		passwordField = new Text(container, SWT.BORDER);
		passwordField.setText(password == null ? "" : password);
		passwordField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		l = new Label(container, SWT.NONE);
		l.setText("Host");
		
		hostField = new Text(container, SWT.BORDER);
		hostField.setText(host == null ? "" : host);
		hostField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		return area;
	}
	
	@Override
	protected void okPressed() {
		IEclipsePreferences prefs = new InstanceScope().getNode("org.eclipse.e4.demo.mailapp");
		prefs.put("username", usernameField.getText());
		prefs.put("password", passwordField.getText());
		prefs.put("host", hostField.getText());
		try {
			prefs.flush();
			super.okPressed();
		} catch (BackingStoreException e) {
			ErrorDialog.openError(getShell(), "Error", "Error while storing preferences", new Status(IStatus.ERROR, "org.eclipse.e4.demo.mailapp", e.getMessage(),e));
		}
	}
}