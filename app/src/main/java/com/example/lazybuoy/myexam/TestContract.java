package com.example.lazybuoy.myexam;

import android.provider.BaseColumns;

public final class TestContract {

    private TestContract() {}

    public static class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "test_categories";
        public static final String COLUMN_NAME = "name";
    }

    public static class TestTable implements BaseColumns{

        public static final String TABLE_NAME = "test_questions";
        public static final String COLUMN_QUESTION = "questions";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_CATEGORY_ID = "category_id";
    }
}
