package com.moriha.shopping_order_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moriha.common.pojo.Address;
import com.moriha.common.pojo.Area;
import com.moriha.common.pojo.City;
import com.moriha.common.pojo.Province;
import com.moriha.common.service.AddressService;
import com.moriha.shopping_order_service.mapper.AddressMapper;
import com.moriha.shopping_order_service.mapper.AreaMapper;
import com.moriha.shopping_order_service.mapper.CityMapper;
import com.moriha.shopping_order_service.mapper.ProvinceMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private ProvinceMapper provinceMapper;

    /*
     * 查询所有省份
     */
    @Override
    public List<Province> findAllProvince() {
        return provinceMapper.selectList(null);
    }

    /**
     * 查询省份下的城市
     */
    @Override
    public List<City> findCityByProvince(Long provinceId) {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("provinceId", provinceId);
        List<City> cities = cityMapper.selectList(queryWrapper);
        return cities;
    }

    /*
     * 查询城市下的区
     */
    @Override
    public List<Area> findAreaByCity(Long cityId) {
        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cityId", cityId);
        List<Area> areas = areaMapper.selectList(queryWrapper);
        return areas;
    }

    /*
     * 添加收货地址
     */
    @Override
    public void add(Address address) {
        addressMapper.insert(address);
    }

    /*
     * 修改收货地址
     */
    @Override
    public void update(Address address) {
        addressMapper.updateById(address);
    }

    /*
     * 根据id查询收货地址
     */
    @Override
    public Address findById(Long id) {
        return addressMapper.selectById(id);
    }

    /*
     * 根据id删除收货地址
     */
    @Override
    public void delete(Long id) {
        addressMapper.deleteById(id);
    }

    /*
     * 查询登录用户的所有地址
     */
    @Override
    public List<Address> findByUser(Long userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Address> addresses = addressMapper.selectList(queryWrapper);
        return addresses;
    }
}
