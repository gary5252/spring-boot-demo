package com.spring.domain;

//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import java.util.List;
//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface LoginRepository extends JpaRepository<Member, Integer> {
	// 驗證會員名稱(帳號)用 > 帳號(名稱)有設unique了所以只會找到一個名稱，沒找到就是沒註冊過
	@Query(value = "select MNAME from MEMBER where MNAME = ?1", nativeQuery = true)
	String findUsername(String mname);

	// 驗證密碼 > 前面先用驗證帳號檢查是否已有會員再透過這邊驗證密碼是否正確(帳號、密碼，DB都設unique了
	// 所以有找到就是密碼也符合唯一的會員資料沒找道就是密碼不正確)
	Member findByMnameAndPassword(String mname, String password);

	// SELECT COUNT(*) FROM MEMBER WHERE MID != ?1 AND MNAME = ?2;
	// 檢查資料重複用SQL > MID != 自己 > MNAME = 修改過後的名稱 > COUNT(*) 大於零代表已有該名稱
	// @Query(value = "SQL") 的 SQL 語句記得不要加分號(;)不然會造成 ?2 被判別錯誤導致
	// 有 org.hibernate.QueryParameterException
	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE MID != ?1 AND MNAME = ?2", nativeQuery = true)
	String checkUniqueMname(int mid, String mname);

	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE MID != ?1 AND PASSWORD = ?2", nativeQuery = true)
	String checkUniquePassword(int mid, String password);

	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE MID != ?1 AND PHONE = ?2", nativeQuery = true)
	String checkUniquePhone(int mid, String phone);

	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE MID != ?1 AND EMAIL = ?2", nativeQuery = true)
	String checkUniqueEmail(int mid, String email);

	// 註冊時還不會有舊資料以及 MID 所以不用篩選 MID 來排除自己
	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE MNAME = ?1", nativeQuery = true)
	String checkRegisterMname(String mname);

	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE PASSWORD = ?1", nativeQuery = true)
	String checkRegisterPassword(String password);

	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE PHONE = ?1", nativeQuery = true)
	String checkRegisterPhone(String phone);

	@Query(value = "SELECT COUNT(*) FROM MEMBER WHERE EMAIL = ?1", nativeQuery = true)
	String checkRegisterEmail(String email);

	// 修改語法記的加入事務和調整的註解
	@Transactional
	@Modifying
	@Query("update Member m set m.password = :password where m.mid = :mid ")
	int updatePwd(@Param("password") String password, @Param("mid") int mid);
}
