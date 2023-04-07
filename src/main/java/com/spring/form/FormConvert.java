package com.spring.form;

// 設置介面讓那些將複合表單轉換實體類別寫入資料庫的方法抽象化遵循統一規則
// <S,T> 泛型，S代表原對象，T代表要轉換的目標對象
public interface FormConvert<S,T> {
	T convert(S s);
}
