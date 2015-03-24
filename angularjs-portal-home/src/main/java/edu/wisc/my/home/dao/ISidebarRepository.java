package edu.wisc.my.home.dao;

import java.util.List;

import edu.wisc.my.home.model.Sidebar;

public interface ISidebarRepository {

    public abstract void add(Sidebar sidebar);

    public abstract List<Sidebar> getAll();

    public abstract void clear();

    public abstract Long rm(Integer index);

}