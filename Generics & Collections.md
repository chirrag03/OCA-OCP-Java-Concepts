# Generics & Collections


### toString(), hashCode(), and equals() 

Some important methods in class java.lang.Object :

![noImage](./img/table_11-1.png)


### The toString() Method

Code calls toString() on your object when it wants to read useful details about your object. For eg, it is invoked when you pass an object reference to the System.out.println() method.

```java

public class HardToRead {
    public static void main(String[] args) {
        HardToRead h = new HardToRead();
        System.out.println(h);
    }
}

//Running the HardToRead class gives us the lovely and meaningful
%java HardToRead
HardToRead @a47e0

```
Because you don’t override the toString() method of class Object, the output is class name followed by the @ symbol, followed by the unsigned hexadecimal representation of the object’s hashcode.

By overriding the toString() method in your classes, we can get a readable output, for example:
```java
public class BobTest {
    public static void main(String[] args) {
        Bob f = new Bob("GoBobGo", 19);
        System.out.println(f);
    }
}
class Bob {
    int shoeSize;
    String nickName;
    Bob(String nickName, int shoeSize) {
        this.shoeSize = shoeSize;
        this.nickName = nickName;
    }
    public String toString() {
        return ("I am a Bob, but you can call me " + nickName +
            ". My shoe size is " + shoeSize);
    }
}

//This ought to be a bit more readable:
%java BobTest
I am a Bob, but you can call me GoBobGo.My shoe size is 19

```

### Overriding equals()
Common uses of the equals() method:

- To sort or search through a collection of objects, the equals() and hashCode() methods are essential. 

- String class has overridden the equals() method (inherited from the class Object), so you could compare two different String objects to see if their contents are equivalent. 

- There is a wrapper class for every kind of primitive. The folks who created the Integer class decided that if two different Integer instances both hold the int value 5, they are equal. The fact that the value 5 lives in two separate objects doesn't matter.

- To know if two object references are identical, use ==. But to know if the attributes of the objects themselves (not the references) are equal, use the equals() method. 

For each class you write, you must decide if it makes sense to consider two different instances equal. 

### What It Means If You Don't Override equals()

Unless you override equals(), two objects are considered equal only if the two references refer to the same object, since the equals() method in class Object uses only the == operator for comparisons.

- You won't be able to use those objects as a key in a hashtable 
- You won't get accurate Sets such that there are no conceptual duplicates. 

**Let's look at what it means to not be able to use an object as a hashtable key.** 
Imagine you have a car (say, John's red Subaru Outback as opposed to Mary's purple Mini). Let’s say you add a car instance as the key to the HashMap (along with a corresponding Person object as the value). But now what happens when you want to search a Person given a car. But now you're in trouble unless you still have a reference to the exact object you used as the key when you added it to the Collection. In other words, you can't make an identical Car object and use it for the search.

**If you want objects of your class to be used as keys for a hashtable (or as elements in any data structure that uses equivalency for searching for—and/or retrieving—an object), then you must override equals() so that two different instances can be considered the same.**

So how would we fix the car? You might override the equals() method so that it compares the unique VIN (Vehicle Identification Number) as the basis of comparison. That way, you can use one instance when you add it to a Collection and essentially re-create an identical instance when you want to do a search based on that object as the key. Of course, overriding the equals() method for Car also allows the potential for more than one object representing a single unique car to exist, which might not be safe in your design. 

**Fortunately, the String and wrapper classes work well as keys in hashtables— they override the equals() method.** So rather than using the actual car instance as the key into the car/owner pair, you could simply use a String that represents the unique identifier for the car. That way, you'll never have more than one instance representing a specific car, but you can still use the car—or rather, one of the car's attributes—as the search key.

### Implementing an equals() Method

```java
public class EqualsTest {
    public static void main(String[] args) {
        Moof one = new Moof(8);
        Moof two = new Moof(8);
        if (one.equals(two)) {
            System.out.println("one and two are equal");
        }
    }
}
class Moof {
    private int moofValue;
    Moof(int val) {
        moofValue = val;
    }
    public int getMoofValue() {
        return moofValue;
    }
    public boolean equals(Object o) {
        if ((o instanceof Moof) && (((Moof) o).getMoofValue() ==
                this.moofValue)) {
            return true;
        } else {
            return false;
        }
    }
}
```

**Problem Statement:** Two Moof objects are the same if their moofValue is identical. So you need to override the equals() method and compare the two Moof instances. 

**What happens in the equals() method ?**
We have to do two things in order to make a valid equality comparison of two Moof objects. 

Firstly, the object being tested comes in polymorphically as type Object, so you need to do an instanceof test on it just to be sure that you could cast the object argument to the correct type. If you skip the instanceof test, then you'll get a runtime ClassCastException when the (Moof)o cast will fail if o doesn't refer to something that IS-A Moof. 

Second, compare the attributes we care about (in this case, just moofValue). 

Casting the object reference, o, is necessary so that you can access its methods or variables in to do the comparison. Without the cast, you can't compile because the compiler would see the object referenced by o as an Object and the Object class doesn't have a getMoofValue().

**A Java contract is a set of rules that must be followed if you want to provide a "correct" implementation as others will expect it to be. If you don't follow the contract, your code may still compile and run, but your code (or someone else's) may break at runtime in some unexpected way.**

**The equals() contract says**
- It is reflexive. For any reference value x, x.equals(x) should return true. 
- It is symmetric. For any reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true. 
- It is transitive. For any reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) must return true. 
- It is consistent. For any reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals() comparisons on the object is modified. 
- For any non-null reference value x, x.equals(null) should return false. 


### Overriding hashCode()
The hashcode value of an object is used by some collection classes. Although you can think of it as kind of an object ID number, it isn't necessarily unique. 

Collections such as HashMap and HashSet use the hashcode value of an object to determine how the object should be stored in the collection, and the hashcode is used again to help locate the object in the collection. 

**Understanding Hashcodes**
Consider the implementation of a HashMap. Basically there is an arrayList where each cell represents a bucket and each array index represents a bucketIndex. A bucket is represented using a linkedlist i.e. each cell will be storing a linkedlist.

Whenever a key value pair need to be put, Hashcode determines in which bucket should the key-value pair be inserted. 

*A hashcode is just an integer generated by an algorithm* that will always run the same way given a specific input, so the output will always be identical for any two identical inputs. 

Now you need to retrieve the value that matches a given key. The hashcode tells you in which bucket you should look to find the key-value pair. 

For example, key is a string value then the a possible hashcode algorithm could be : You take the string and calculate an integer code from it by using A is 1, B is 2, and so on, adding the numeric values of all the letters in the string together. 

![Sorry. Image not loaded](./img/hashcode_example.png)

Note: Two different names might result in the same value. That's acceptable. The hashcode tells you only which bucket to go into and not how to locate the name once we're in that bucket.
You'll still have to search through the target bucket, reading each name until we find the desired name.


**In real-life hashing, it's not uncommon to have more than one entry in a bucket. Hashing retrieval is a two-step process.** 
**1. Find the right bucket (using hashCode()).** 
**2. Search the bucket for the right element (using equals())** 

When you put an object in a collection that uses hashcodes, the collection uses the hashcode of the object to decide in which bucket/slot the object should land. Then when you want to fetch that object (or, for a hashtable, retrieve the associated value for that object), you have to give the collection a reference to an object, which it then compares to the objects it holds in the collection. As long as the object stored in the collection, you're trying to search for has the same hashcode as the object you're using for the search, then the object will be found. Otherwise not. 

**If two objects are equal, their hashcodes must be equal as well. Two unequal objects may or may not have different hashcodes.**

### Implementing hashCode()
```java
class HasHash {
    public int x;
    HasHash(int xVal) {
        x = xVal;
    }
    public boolean equals(Object o) {
        HasHash h = (HasHash) o; // Don't try at home without
        // instanceof test
        if (h.x == this.x) {
            return true;
        } else {
            return false;
        }
    }
    public int hashCode() {
        return (x * 17);
    }
}
```

Typically, you'll see hashCode() methods that do some combination of ^-ing (XOR-ing) a class's instance variables (in other words, twiddling their bits), along with perhaps multiplying them by a prime number. In any case, while the goal is to get a wide and random distribution of objects across buckets, **the contract requires only that two equal objects have equal hashcodes.**

So in order for an object to be located, the search object and the object in the collection must both have identical hashcode values and return true for the equals() method. 
So you must override both methods to be absolutely certain that your objects can be used in Collections that use hashing.

**The hashCode() Contract**
**-Whenever it is invoked on the same object more than once during an execution of a Java application, the hashCode() method must consistently return the same integer, provided that no information used in equals() comparisons on the object is modified.**
**-If two objects are equal according to the equals(Object) method, then calling the hashCode() method on each of the two objects must produce the same integer result.**
**-It is NOT required that if two objects are unequal according to the equals(java.lang.Object) method, then calling the hashCode() method on each of the two objects must produce distinct integer results.**


![Sorry. Image not loaded](./img/hashcode_equals_table.png)

**What happens if you include a transient variable in your hashCode() method?**
**While that's legal (the compiler won't complain), under some circumstances, an object you put in a collection won't be found. As you might know, serialization saves an object so that it can be reanimated later by deserializing it back to full objectness. But transient variables are not saved when an object is serialized.**

```java
class SaveMe implements Serializable {
    transient int x;
    int y;
    SaveMe(int xVal, int yVal) {
        x = xVal;
        y = yVal;
    }
    public int hashCode() {
        return (x ^ y); // Legal, but not correct to
        // use a transient variable
    }
    public boolean equals(Object o) {
        SaveMe test = (SaveMe) o;
        if (test.y == y && test.x == x) { // Legal, not correct
            return true;
        } else {
            return false;
        }
    }
}
```

Here's what could happen using code like the preceding example: 
1. Give an object some state (assign values to its instance variables). 
2. Put the object in a HashMap, using the object as a key. 
3. Save the object to a file using serialization without altering any of its state. 
4. Retrieve the object from the file through deserialization. 
5. Use the deserialized object to get the object out of the HashMap.


Oops. The object in the collection and the supposedly same object brought back to life are no longer identical. The object's transient variable will come back with a default value rather than the value the variable had at the time it was saved (or put into the HashMap). 
So using the preceding SaveMe code, if the value of x is 9 when the instance is put in the HashMap, then since x is used in the calculation of the hashcode, when the value of x changes, the hashcode changes too. And when that same instance of SaveMe is brought back from deserialization, x == 0, regardless of the value of x at the time the object was serialized. So the new hashcode calculation will give a different hashcode and the equals() method fails as well since x is used to determine object equality.


**Bottom line: transient variables can really mess with your equals() and hashCode() implementations. Keep variables non-transient or, if they must be marked transient, don't use them to determine hashcodes or equality.**
