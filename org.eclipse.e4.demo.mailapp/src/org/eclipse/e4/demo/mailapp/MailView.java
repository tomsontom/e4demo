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
 * and is available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

@SuppressWarnings("restriction")
public class MailView {
	private DataBindingContext dbc;
	private WritableValue mail = new WritableValue();
	private ObservablesManager manager;
		
	@Inject
	public MailView(final Composite composite, @Optional final IStylingEngine styleingEngine) {
		dbc = new DataBindingContext();
		manager = new ObservablesManager();
		
		manager.runAndCollect(new Runnable() {
			
			public void run() {
				initUI(composite, styleingEngine);
			}
		});
	}
	
	@Inject
	public void setMail(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IMail mail) {
		if( mail != null ) {
			this.mail.setValue(mail);	
		}
	}
	
	private void initUI(Composite composite, IStylingEngine styleingEngine) {
		Composite parent = new Composite(composite, SWT.NONE);
		GridLayout gd = new GridLayout();
		gd.horizontalSpacing=0;
		gd.verticalSpacing=0;
		parent.setLayout(gd);
		
		Composite header = new Composite(parent,SWT.NONE);
		header.setLayout(new GridLayout(2,false));
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if( styleingEngine != null ) {
			styleingEngine.setClassname(header, "mailHeader");	
		}
		
		Label l = new Label(header, SWT.NONE);
		l.setText("From");
		
		l = new Label(header, SWT.NONE);
		l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dbc.bindValue(WidgetProperties.text().observe(l), BeanProperties.value("from").observeDetail(mail));
		
		l = new Label(header,SWT.NONE);
		l.setText("Subject");
		
		l = new Label(header, SWT.BORDER);
		l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dbc.bindValue(WidgetProperties.text().observe(l), BeanProperties.value("subject").observeDetail(mail));
		
		l = new Label(header,SWT.NONE);
		l.setText("To");
		
		l = new Label(header, SWT.NONE);
		l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dbc.bindValue(WidgetProperties.text().observe(l), BeanProperties.value("to").observeDetail(mail));
		
		Text t = new Text(parent, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		t.setLayoutData(new GridData(GridData.FILL_BOTH));
		t.setEditable(false);
		dbc.bindValue(WidgetProperties.text().observe(t), BeanProperties.value("body").observeDetail(mail));
	}
	
	@PreDestroy
	public void dipose() {
		manager.dispose();
	}
}
