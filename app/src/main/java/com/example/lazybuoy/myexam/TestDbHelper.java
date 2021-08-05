package com.example.lazybuoy.myexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lazybuoy.myexam.TestContract.*;
import java.util.ArrayList;
import java.util.List;

public class TestDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="MYEXAM.db";
    private static final int DATABASE_VERSION = 1;

    private static TestDbHelper instance;

    private SQLiteDatabase db;

    public TestDbHelper( Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static synchronized TestDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TestDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                TestTable.TABLE_NAME + " ( " +
                TestTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TestTable.COLUMN_QUESTION + " TEXT," +
                TestTable.COLUMN_OPTION1 + " TEXT, " +
                TestTable.COLUMN_OPTION2 + " TEXT, " +
                TestTable.COLUMN_OPTION3 + " TEXT, " +
                TestTable.COLUMN_ANSWER + " INTEGER, " +
                TestTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + TestTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        fillCategoriesTable();
        fillTestTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TestTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("PPL");
        addCategory(c1);
        Category c2 = new Category("MicroProcessor");
        addCategory(c2);
        Category c3 = new Category("CG");
        addCategory(c3);
    }

    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillTestTable()
    {

        Question q1 = new Question("Object is changing its shape from one form to another form is know as -","Animation","Morphing","Image Transformation",2,Category.CG);
        addQuestion(q1);

        Question q2 = new Question("Color information can be stored in","Main Memory","Secondary Memory","Frame Buffer",3,Category.CG );
        addQuestion(q2);

        Question q3 = new Question("In 80386DX,a logical address is known as _____ which consist of a selector and an offset","Physical Address","Logical Address","Virtual Address",3,Category.MicroProcessor );
        addQuestion(q3);

        Question q4 = new Question("Global Descriptor Table Register(GDTR) is a _____ register located inside the 80386DX   ","50 byte","48 bit","48 byte",2,Category.MicroProcessor );
        addQuestion(q4);

        Question q5 = new Question("During instruction fetch _____ and _____ register are used.","IP,DS","SS,IP","CS,IP",3,Category.MicroProcessor );
        addQuestion(q5);

        Question q6 = new Question("Paging is the _____ phase of address translation.","First","Second","Third",3,Category.MicroProcessor );
        addQuestion(q6);

        Question q7 = new Question("Which of the following is not an Interrupt signal?","NA#","INTR","RESET",1,Category.MicroProcessor );
        addQuestion(q7);

        Question q8 = new Question("TLB consists of _____ sets of table entries.","8","16","32",3,Category.MicroProcessor );
        addQuestion(q8);

        Question q9 = new Question("The instruction mov ax,[bx] is an example of","Register Indirect Addressing Mode","Register Relative Addressing Mode","Register Addressing Mode",1,Category.MicroProcessor );
        addQuestion(q9);

        Question q10 = new Question("What is INT3?","Overflow","Breakpoint","Debug Exceptions",2,Category.MicroProcessor );
        addQuestion(q10);

        Question q11 = new Question("What is the function of CLTS?", "Clears Task-Switched Flag", "Controls Registers of Task Segment", "Controls LDTR bit of Task Segment ", 1,Category.MicroProcessor );
        addQuestion(q11);

        Question q12 = new Question("_____ is the ability for a message or data to be processed in more than one form.", "Abstraction", "Class", "Polymorphism", 3, Category.PPL);
        addQuestion(q12);

        Question q13 = new Question("_____ provides a value for a variable", "Assignment Statement", "Declaration Statement", "Definition Statement", 1,Category.PPL);
        addQuestion(q13);

        Question q14 = new Question("The operator ?: is", "Conditional Operator", "Logical Operator", "Relational Operator", 1,Category.PPL);
        addQuestion(q14);

        Question q15 = new Question("Java has _____ primitive types of data.", "7", "8", "5", 2,Category.PPL);
        addQuestion(q15);
    }

    private void addQuestion(Question question)
    {
        ContentValues cv = new ContentValues();
        cv.put(TestTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(TestTable.COLUMN_OPTION1, question.getOption1());
        cv.put(TestTable.COLUMN_OPTION2, question.getOption2());
        cv.put(TestTable.COLUMN_OPTION3, question.getOption3());
        cv.put(TestTable.COLUMN_ANSWER, question.getAnswer());
        cv.put(TestTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(TestTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions()
    {
        ArrayList<Question> questionList = new ArrayList<>();

        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TestTable.TABLE_NAME, null);

        if (c.moveToFirst())
        {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(TestTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(TestTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(TestTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(TestTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(TestTable.COLUMN_OPTION3)));
                question.setAnswer(c.getInt(c.getColumnIndex(TestTable.COLUMN_ANSWER)));
                question.setCategoryID(c.getInt(c.getColumnIndex(TestTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);

            }while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = TestTable.COLUMN_CATEGORY_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID)};

        Cursor c = db.query(
                TestTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(TestTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(TestTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(TestTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(TestTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(TestTable.COLUMN_OPTION3)));
                question.setAnswer(c.getInt(c.getColumnIndex(TestTable.COLUMN_ANSWER)));
                question.setCategoryID(c.getInt(c.getColumnIndex(TestTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

}
