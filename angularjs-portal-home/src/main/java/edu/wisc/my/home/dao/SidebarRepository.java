package edu.wisc.my.home.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import edu.wisc.my.home.model.Sidebar;

@Repository
public class SidebarRepository implements ISidebarRepository {
    
    private static final String KEY = "global:sidebar";

    @Autowired
    private RedisTemplate<String, Sidebar> template;
    
    /* (non-Javadoc)
     * @see edu.wisc.my.home.dao.ISidebarRepository#add(edu.wisc.my.home.model.Sidebar)
     */
    @Override
    public void add(Sidebar sidebar) {
        template.opsForList().rightPush(KEY, sidebar);
    }
    
    /* (non-Javadoc)
     * @see edu.wisc.my.home.dao.ISidebarRepository#getAll()
     */
    @Override
    public List<Sidebar> getAll() {
        return template.opsForList().range(KEY, 0, -1);
    }
    
    @Override
    public void clear() {
        template.delete(KEY);
    }

    @Override
    public Long rm(Integer index) {
        Sidebar sb = template.opsForList().index(KEY, index);
        return template.opsForList().remove(KEY, 1, sb);
        
    }
}
