package org.example;
import java.lang.reflect.Constructor;

enum ChildClasses {
    CHILD1("ChildClass1", ChildClass1.class),
    CHILD2("ChildClass2", ChildClass2.class),
    CHILD3("ChildClass3", ChildClass3.class);

    private final String name;
    private final Class<?> clazz;

    ChildClasses(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
class ParentClass {
    String parentState;
    Object[] children;

    ParentClass(String state) {
        this.parentState = state;
        System.out.println("ParentClass Constructor: State = " + state);


        children = new Object[ChildClasses.values().length];

        int i = 0;
        for (ChildClasses childEnum : ChildClasses.values()) {
            try {
                Constructor<?> constructor = childEnum.getClazz().getConstructor(ParentClass.class);
                children[i] = constructor.newInstance(this); // Instantiate the child
                i++;
            } catch (Exception e) {
                System.out.println("Failed to initialize: " + childEnum.getName());
                e.printStackTrace();
            }
        }


        for (Object child : children) {
            if (child instanceof Initializable) {
                ((Initializable) child).initialize();
            }
        }
    }
}

interface Initializable {
    void initialize();
}

class ChildClass1 implements Initializable {
    ParentClass parent;

    public ChildClass1(ParentClass parent) {
        this.parent = parent;
        System.out.println("ChildClass1 received ParentState: " + parent.parentState);
    }

    @Override
    public void initialize() {

        if (parent.children[2] instanceof ChildClass3) {
            ChildClass3 child3 = (ChildClass3) parent.children[2];
            System.out.println("ChildClass1 accessed ChildClass3's State: " + child3.child3State);
        }
    }
}

class ChildClass2 {
    public ChildClass2(ParentClass parent) {
        System.out.println("ChildClass2 received ParentState: " + parent.parentState);
    }
}

class ChildClass3 {
    String child3State = "This is ChildClass3's State";

    public ChildClass3(ParentClass parent) {
        System.out.println("ChildClass3 initialized with State: " + child3State);
    }
}

public class Main {
    public static void main(String[] args) {
        ParentClass parent = new ParentClass("Parent State Value");
    }
}
