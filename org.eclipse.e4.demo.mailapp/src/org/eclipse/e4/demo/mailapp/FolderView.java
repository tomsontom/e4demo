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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class FolderView {
	private TableViewer viewer;
	
	public FolderView(Composite parent) {
		this.viewer = new TableViewer(parent);
		this.viewer.setContentProvider(new ArrayContentProvider());
		this.viewer.getTable().setHeaderVisible(true);
		this.viewer.getTable().setLinesVisible(true);
		
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Subject");
		column.getColumn().setWidth(250);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IMail)element).getSubject();
			}
		});
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("From");
		column.getColumn().setWidth(200);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IMail)element).getFrom();
			}
		});
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Date");
		column.getColumn().setWidth(150);
		column.setLabelProvider(new ColumnLabelProvider() {
			private DateFormat format = SimpleDateFormat.getDateTimeInstance();
			@Override
			public String getText(Object element) {
				Date date = ((IMail)element).getDate();
				if( date != null ) {
					return format.format(date);	
				}
				return "-";
			}
		});
	}
	
	public void setFolder(IFolder folder) {
		if( folder != null ) {
			viewer.setInput(folder.getSession().getMails(folder, 0, folder.getMailCount()));	
		}
	}
}