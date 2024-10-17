package com.ml.cart.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ml.api.client.ItemClient;
import com.ml.api.dto.ItemDTO;
import com.ml.cart.domain.dto.CartFormDTO;
import com.ml.cart.domain.po.Cart;
import com.ml.cart.domain.vo.CartVO;
import com.ml.cart.mapper.CartMapper;
import com.ml.cart.service.ICartService;
import com.hmall.common.exception.BizIllegalException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.common.utils.CollUtils;
import com.hmall.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    /**
     * // private final IItemService itemService;
     *
     * // 必备字段, 即必须初始化, 使用@ReuqirdArgsConstructor, 会给没有初始化的字段进行构造函数初始化
     * private final RestTemplate restTemplate;
     *
     * private final DiscoveryClient discoveryClient;
     */
    private final ItemClient itemClient;

    @Override
    public void addItem2Cart(CartFormDTO cartFormDTO) {
        // 1.获取登录用户
        Long userId = UserContext.getUser();

        // 2.判断是否已经存在
        if(checkItemExists(cartFormDTO.getItemId(), userId)){
            // 2.1.存在，则更新数量
            baseMapper.updateNum(cartFormDTO.getItemId(), userId);
            return;
        }
        // 2.2.不存在，判断是否超过购物车数量
        checkCartsFull(userId);

        // 3.新增购物车条目
        // 3.1.转换PO
        Cart cart = BeanUtils.copyBean(cartFormDTO, Cart.class);
        // 3.2.保存当前用户
        cart.setUserId(userId);
        // 3.3.保存到数据库
        save(cart);
    }

    @Override
    public List<CartVO> queryMyCarts() {
        // 1.查询我的购物车列表
        List<Cart> carts = lambdaQuery().eq(Cart::getUserId, 1L/* TODO: UserContext.getUser()*/).list();
        if (CollUtils.isEmpty(carts)) {
            return CollUtils.emptyList();
        }

        // 2.转换VO
        List<CartVO> vos = BeanUtils.copyList(carts, CartVO.class);

        // 3.处理VO中的商品信息
        handleCartItems(vos);

        // 4.返回
        return vos;
    }

    private void handleCartItems(List<CartVO> vos) {
        
         // TODO 1.获取商品id
        List<Long> itemIds = vos.stream().map(CartVO::getItemId).collect(Collectors.toList());
        
        /**
         * // 2.查询商品
         *  List<ItemDTO> items = itemService.queryItemByIds(itemIds);
         *
         * // 2.1 根据服务名称获取服务列表
         * List<ServiceInstance> instances = discoveryClient.getInstances("item-service");
         *
         * // 2.2 手写负载均衡, 挑选一个服务实例
         * ServiceInstance instance = instances.get(RandomUtil.randomInt(instances.size()));
         *
         * // 2.1 给item-service发请求,查询商品
         * ResponseEntity<List<ItemDTO>> response = restTemplate.exchange(
         *         //"http://localhost:8089"
         *         instance.getUri() + "/items?ids={ids}",
         *         // 请求方式, 枚举类型
         *         HttpMethod.GET,
         *         // 请求实体
         *         null,
         *         // 返回值类型 List<ItemDTO> ,使用反射获取
         *         new ParameterizedTypeReference<List<ItemDTO>>() {
         *         },
         *         // 参数 ids
         *         Map.of("ids", CollUtil.join(itemIds, ","))
         * );
         * // 2.2 解析响应
         * // 判断是否请求成功
         * if (!response.getStatusCode().is2xxSuccessful()) {
         *     // 查询失败
         *     return;
         * }
         * List<ItemDTO> items = response.getBody();
         */
        List<ItemDTO> items = itemClient.queryItemByTds(itemIds);
        
        if (CollUtils.isEmpty(items)) {
            return;
        }
        // 3.转为 id 到 item的map
        Map<Long, ItemDTO> itemMap = items.stream().collect(Collectors.toMap(ItemDTO::getId, Function.identity()));
        // 4.写入vo
        for (CartVO v : vos) {
            ItemDTO item = itemMap.get(v.getItemId());
            if (item == null) {
                continue;
            }
            v.setNewPrice(item.getPrice());
            v.setStatus(item.getStatus());
            v.setStock(item.getStock());
        }
    }

    @Override
    public void removeByItemIds(Collection<Long> itemIds) {
        // 1.构建删除条件，userId和itemId
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
        queryWrapper.lambda()
                .eq(Cart::getUserId, UserContext.getUser())
                .in(Cart::getItemId, itemIds);
        // 2.删除
        remove(queryWrapper);
    }

    private void checkCartsFull(Long userId) {
        Long count = lambdaQuery().eq(Cart::getUserId, userId).count();
        if (count >= 10) {
            throw new BizIllegalException(StrUtil.format("用户购物车课程不能超过{}", 10));
        }
    }

    private boolean checkItemExists(Long itemId, Long userId) {
        Long count = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getItemId, itemId)
                .count();
        return count > 0;
    }
}
