package com.gem.service.impl;

import com.gem.mapper.PropertyValueMapper;
import com.gem.pojo.Product;
import com.gem.pojo.Property;
import com.gem.pojo.PropertyValue;
import com.gem.pojo.PropertyValueExample;
import com.gem.service.PropertyService;
import com.gem.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueMapper propertyValueMapper;

    @Autowired
    PropertyService propertyService;

    /**初始化PropertyValue**/
    @Override
    public void init(Product p) {
         List<Property> pts = propertyService.list(p.getCid());

         for(Property pt:pts){
             PropertyValue pv = get(pt.getId(),p.getId());
             if(null==pv){
                 pv = new PropertyValue();
                 pv.setPid(p.getId());
                 pv.setPtid(pt.getId());
                 propertyValueMapper.insert(pv);
             }
         }
    }

    @Override
    public void update(PropertyValue pv) {
        propertyValueMapper.updateByPrimaryKeySelective(pv);
    }

    /**根据属性id和产品id获取PropertyValue对象**/
    @Override
    public PropertyValue get(int ptid, int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(ptid).andPidEqualTo(pid);
        List<PropertyValue> pvs = propertyValueMapper.selectByExample(example);
        if(pvs.isEmpty()){
            return  null;
        }
        return pvs.get(0);
    }

    @Override
    public List<PropertyValue>  list(int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> result = propertyValueMapper.selectByExample(example);
        for(PropertyValue pv:result){
            Property property = propertyService.get(pv.getPtid());
            pv.setProperty(property);
        }
        return  result;
    }
}
