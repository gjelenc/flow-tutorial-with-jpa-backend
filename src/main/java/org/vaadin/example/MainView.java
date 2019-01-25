/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The main view contains a button and a click listener.
 */
@Route
public class MainView extends VerticalLayout {

    @Inject
    private CustomerService service;
    
    @Inject
    private CustomerForm form;
    
    private Grid<Customer> grid = new Grid<>();
    private TextField filterText = new TextField();
    
    public MainView() {
        
    }

    @PostConstruct
    void init() {
        form.setMainView(this);
        
        filterText.setPlaceholder("Filter by name...");
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());
        
        Button clearFilterTextBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterTextBtn.addClickListener(e -> filterText.clear());
        HorizontalLayout filtering = new HorizontalLayout(filterText, clearFilterTextBtn);
        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setCustomer(new Customer());
        });
        add(filtering, grid);
        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn);
        
        grid.setSizeFull();
        grid.addColumn(Customer::getFirstName).setHeader("First name");
        grid.addColumn(Customer::getLastName).setHeader("Last name");
        grid.addColumn(Customer::getStatus).setHeader("Status");
        
        HorizontalLayout main = new HorizontalLayout(grid , form);
        main.setAlignItems(Alignment.START);
        main.setSizeFull();
        add(toolbar, main);
        add(filtering, main);
        setHeight("100vh");
        updateList();
        grid.asSingleSelect().addValueChangeListener((event) -> {
            form.setCustomer(event.getValue());
        });
    }

    public void updateList() {
        /**
         * Note that filterText.getValue() might return null; in this case, the backend
         * takes care of it for us
         */
        grid.setItems(service.findAll(filterText.getValue()));
    }
    
}
