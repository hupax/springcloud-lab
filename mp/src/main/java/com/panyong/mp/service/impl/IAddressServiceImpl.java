package com.panyong.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panyong.mp.domain.po.Address;
import com.panyong.mp.service.IAddressService;
import com.panyong.mp.mapper.AddressMapper;
import org.springframework.stereotype.Service;

/**
* @author panyong
* @description 针对表【address】的数据库操作Service实现
* @createDate 2024-10-02 15:25:39
*/

@Service
public class IAddressServiceImpl extends ServiceImpl<AddressMapper, Address>
    implements IAddressService {

}




