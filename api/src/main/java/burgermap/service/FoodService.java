package burgermap.service;

import burgermap.entity.Burger;
import burgermap.entity.Food;
import burgermap.entity.Member;
import burgermap.entity.SideMenu;
import burgermap.entity.Store;
import burgermap.enums.MemberType;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.repository.FoodRepository;
import burgermap.repository.MemberRepository;
import burgermap.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public Burger addBurger(Long memberId, Burger burger) {
        Member member = memberRepository.findByMemberId(memberId).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }

        Store store = storeRepository.findByStoreId(burger.getStoreId()).orElse(null);
        if (store == null) {  // 존재하지 않는 가게
            return null;
        }

        Food savedFood = foodRepository.save(burger);
        Burger savedBurger = (Burger) savedFood;
        log.debug("burger added: {}", savedBurger);
        return savedBurger;
    }

    public SideMenu addSideMenu(Long memberId, SideMenu sideMenu) {
        Member member = memberRepository.findByMemberId(memberId).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }

        Store store = storeRepository.findByStoreId(sideMenu.getStoreId()).orElse(null);
        if (store == null) {  // 존재하지 않는 가게
            return null;
        }

        Food savedFood = foodRepository.save(sideMenu);
        SideMenu savedSideMenu = (SideMenu) savedFood;
        log.debug("SideMenu added: {}", savedSideMenu);
        return savedSideMenu;
    }

    public List<Food> getFoods(Long storeId) {
        List<Food> foods = foodRepository.findByStoreId(storeId);
        log.debug("foods: {}", foods);
        return foods;
    }


}
