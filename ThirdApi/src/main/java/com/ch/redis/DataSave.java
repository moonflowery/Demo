package com.ch.redis;

import java.io.*;
import java.util.ArrayList;

/**
 * 面试题：我们知道文件存储可以存储一些数据，我们现在想要利用文件存储的方法，来构建一类类似于redis的持久化存储类。
 * 它可以存储不同类型的对象，并且可以设置过期时间，当过期时间到达时，对象会被自动删除或不可访问。
 * 注意，这里的存储对象期望可以是尽可能支持广泛类型的对象，而不仅仅是特定的类型的对象。
 * 请实现以下的DataSave类的save和load方法以实现我们的目标，并保证unitest方法中的测试通过。（可以添加其他的辅助方法及类）
 * <p>
 * 提示：实现以下问题的方法很多，并没有唯一答案，请尽可能提供简洁的实现。我们重点关注代码的可读性和可维护性及思路。
 * <p>
 * 提交格式：请提供实现的代码，并且提供运行结果的截图。
 */
public class DataSave {


    //请实现持久化存储函数（使用文件存储相关方法）

    public static void writeObjectToFile(String key, ObjectWrapper obj) {
        File file = new File(key + ".dat");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObjectWrapper readObjectFromFile(String key)
    {
        ObjectWrapper temp=null;
        File file =new File(key+".dat");
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp = (ObjectWrapper) objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * @param key    存储的key
     * @param s      存储的对象
     * @param expire 过期时间，单位秒，如果为0则表示永不过期
     */
    void save(String key, Object s, int expire) {
        /**
         * 你的代码
         */
        ObjectWrapper wrapper = new ObjectWrapper();
        if (expire!=0){
            wrapper.setExpire(System.currentTimeMillis()+expire*1000);
        }
        wrapper.setTargetObject(s);
        writeObjectToFile(key, wrapper);
    }

    //请实现持久化数据的取出

    /**
     * @param key 存储的key
     * @return 存储的对象
     */
    Object load(String key) {
        /**
         * 你的代码
         */
        ObjectWrapper objectWrapper = readObjectFromFile(key);
        Long expire = objectWrapper.getExpire();
        if (expire!=null && expire<System.currentTimeMillis()){
            return null;
        }
        return objectWrapper.getTargetObject();
    }

    public static void main(String[] args) {
        unitest();
    }

    static void unitest() {
        School sc = new School("wuhan", "wuhan location");
        Clazz c = new Clazz("1", 30, 2, sc);
        Student s = new Student("zhangsan", 18, c);
        Student s0 = new Student("lisi", 22, c);

        //存储和取出学生对象
        DataSave sds = new DataSave();
        sds.save("student", s, 0);
        Student s2 = (Student) (sds.load("student"));
        System.out.println("age:" + s2.age);
        System.out.println("grade:" + s2.clazz.grade);
        System.out.println("address:" + s2.clazz.school.address);

        //存储和取出班级对象
        sds.save("clazz", c, 0);
        Clazz c2 = (Clazz) (sds.load("clazz"));
        System.out.println("grade:" + c2.grade);
        System.out.println("address:" + c2.school.address);

        //存储和取出学校对象
        sds.save("school", sc, 0);
        School sc2 = (School) (sds.load("school"));
        System.out.println("address:" + sc2.address);

        //存储和取出学生列表
        ArrayList<Student> students = new ArrayList<Student>();
        students.add(s);
        students.add(s0);
        sds.save("students", students, 0);
        ArrayList<Student> students2 = (ArrayList<Student>) (sds.load("students"));
        System.out.println("students size:" + students2.size());
        System.out.println("students1 age:" + students2.get(0).age);


        //存储和取出学生对象，过期时间为10秒
        sds.save("school_test", sc, 10);
        School sc3 = (School) (sds.load("school_test"));
        System.out.println("未过期时，school:" + sc3);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        School sc4 = (School) (sds.load("school_test"));
        System.out.println("已过期时，school:" + (sc4 == null));

    }
}

class ObjectWrapper implements Serializable{
    private Long expire;
    private Object targetObject;

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }
}


class Student implements Serializable{
    String name;
    int age;
    Clazz clazz;

    public Student(String name, int age, Clazz clazz) {
        this.name = name;
        this.age = age;
        this.clazz = clazz;
    }
}

class Clazz implements Serializable{
    String grade;
    int studentNumbers;
    int teacherNumbers;
    School school;

    public Clazz(String grade, int studentNumbers, int teacherNumbers, School school) {
        this.grade = grade;
        this.studentNumbers = studentNumbers;
        this.teacherNumbers = teacherNumbers;
        this.school = school;
    }
}

class School implements Serializable{
    String name;
    String address;

    public School(String name, String address) {
        this.name = name;
        this.address = address;
    }
}