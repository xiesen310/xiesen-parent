package com.github.xiesen.builder;

import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * @author 谢森
 * @since 2021/2/20
 */
@Data
@ToString
public class GirlFriend2 {
    private String name;
    private int age;
    private int bust;
    private int waist;
    private int hips;
    private List<String> hobby;
    private String birthday;
    private String address;
    private String mobile;
    private String email;
    private String hairColor;
    private Map<String, String> gift;

    /**
     * 添加爱好
     *
     * @param hobby
     */
    public void addHobby(String hobby) {
        this.hobby = Optional.ofNullable(this.hobby).orElse(new ArrayList<>());
        this.hobby.add(hobby);
    }

    /**
     * 添加图片
     *
     * @param day
     * @param gift
     */
    public void addGift(String day, String gift) {
        this.gift = Optional.ofNullable(this.gift).orElse(new HashMap<>());
        this.gift.put(day, gift);
    }

    public void setVitalStatistics(int bust, int waist, int hips) {
        this.bust = bust;
        this.waist = waist;
        this.hips = hips;
    }

    public static void main(String[] args) {
        Builder.of(GirlFriend2::new)
                .with(GirlFriend2::setAge, 10).build();

        GirlFriend2 myGirlFriend = Builder.of(GirlFriend2::new)
                .with(GirlFriend2::setName, "小美")
                .with(GirlFriend2::setAge, 18)
                .with(GirlFriend2::setVitalStatistics, 33, 23, 33)
                .with(GirlFriend2::setBirthday, "2001-10-26")
                .with(GirlFriend2::setAddress, "上海浦东")
                .with(GirlFriend2::setMobile, "18688888888")
                .with(GirlFriend2::setEmail, "pretty-xiaomei@qq.com")
                .with(GirlFriend2::setHairColor, "浅棕色带点微卷")
                .with(GirlFriend2::addHobby, "逛街")
                .with(GirlFriend2::addHobby, "购物")
                .with(GirlFriend2::addHobby, "买东西")
                .with(GirlFriend2::addGift, "情人节礼物", "LBR 1912女王时代")
                .with(GirlFriend2::addGift, "生日礼物", "迪奥烈焰蓝金")
                .with(GirlFriend2::addGift, "纪念日礼物", "阿玛尼红管唇釉")
                .build();

        System.out.println(myGirlFriend.toString());
    }
}
