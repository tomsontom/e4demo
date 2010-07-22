package org.eclipse.e4.demo.mailapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

@SuppressWarnings("restriction")
public class FolderView {
	private TableViewer viewer;
	
	@Inject
	@Optional
	private ESelectionService selectionService;
	
	@Inject
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
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				if( selectionService != null ) {
					selectionService.setSelection(((IStructuredSelection)event.getSelection()).getFirstElement());
				}
			}
		});
	}
	
	@Inject
	public void setFolder(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IFolder folder) {
		if( folder != null ) {
			viewer.setInput(folder.getSession().getMails(folder, 0, folder.getMailCount()));
		}
	}
}