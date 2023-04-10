package org.joinfaces.example.view;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.joinfaces.example.model.Customer;
import org.joinfaces.example.model.CustomerStatus;
import org.joinfaces.example.model.Representative;
import org.joinfaces.example.service.CustomerService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.util.LangUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@Named("dtFilterView")
@ViewScoped
public class FilterView implements Serializable {
    @Inject
    private CustomerService service;

    private List<Customer> customers1;

    private List<Customer> customers2;

    private List<Customer> customers3;

    private List<Customer> filteredCustomers1;

    private List<Customer> filteredCustomers2;

    private List<Customer> filteredCustomers3;

    private List<FilterMeta> filterBy;

    private boolean globalFilterOnly;

    @PostConstruct
    public void init() {
        globalFilterOnly = false;
        customers1 = service.getCustomers(10);
        customers2 = service.getCustomers(50);
        customers3 = service.getCustomers(10);

        filterBy = new ArrayList<>();

        filterBy.add(FilterMeta.builder()
                .field("status")
                .filterValue(CustomerStatus.NEW)
                .matchMode(MatchMode.EQUALS)
                .build());

        filterBy.add(FilterMeta.builder()
                .field("date")
                .filterValue(new ArrayList<>(Arrays.asList(LocalDate.now().minusDays(28), LocalDate.now().plusDays(28))))
                .matchMode(MatchMode.BETWEEN)
                .build());

    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        Customer customer = (Customer) value;
        return customer.getName().toLowerCase().contains(filterText)
                || customer.getCountry().getName().toLowerCase().contains(filterText)
                || customer.getRepresentative().getName().toLowerCase().contains(filterText)
                || customer.getDate().toString().toLowerCase().contains(filterText)
                || customer.getStatus().name().toLowerCase().contains(filterText)
                || customer.getActivity() < filterInt;
    }

    public void toggleGF() {
        System.out.println("in toggleGF method");
    }

    public void toggleGlobalFilter() {
        setGlobalFilterOnly(!isGlobalFilterOnly());
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public List<Representative> getRepresentatives() {
        return service.getRepresentatives();
    }

    public CustomerStatus[] getCustomerStatus() {
        return service.getCustomerStatus();
    }

    public List<Customer> getCustomers1() {
        return customers1;
    }

    public List<Customer> getCustomers2() {
        return customers2;
    }

    public List<Customer> getCustomers3() {
        return customers3;
    }

    public List<Customer> getFilteredCustomers1() {
        return filteredCustomers1;
    }

    public void setFilteredCustomers1(List<Customer> filteredCustomers1) {
        this.filteredCustomers1 = filteredCustomers1;
    }

    public List<Customer> getFilteredCustomers2() {
        return filteredCustomers2;
    }

    public void setFilteredCustomers2(List<Customer> filteredCustomers2) {
        this.filteredCustomers2 = filteredCustomers2;
    }

    public List<Customer> getFilteredCustomers3() {
        return filteredCustomers3;
    }

    public void setFilteredCustomers3(List<Customer> filteredCustomers3) {
        this.filteredCustomers3 = filteredCustomers3;
    }

    public void setService(CustomerService service) {
        this.service = service;
    }

    public List<FilterMeta> getFilterBy() {
        return filterBy;
    }

    public boolean isGlobalFilterOnly() {
        return globalFilterOnly;
    }

    public void setGlobalFilterOnly(boolean globalFilterOnly) {
        this.globalFilterOnly = globalFilterOnly;
    }

    public List<String> getColumnList() {
        return Arrays.stream(Customer.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("serialVersionUID"))
                .map(Field::getName)
                .toList();
    }

}
