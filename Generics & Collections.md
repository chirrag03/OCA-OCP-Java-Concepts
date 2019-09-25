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

**Fortunately, the String and wrapper classes work well as keys in hashtables— they override the equals() method.**  
So rather than using the actual car instance as the key into the car/owner pair, you could simply use a String that represents the unique identifier for the car. That way, you'll never have more than one instance representing a specific car, but you can still use the car—or rather, one of the car's attributes—as the search key.

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

Whenever a key value pair need to be put, Hashcode of the key determines in which bucket should the key-value pair be inserted. 
Now you need to retrieve the value that matches a given key. The hashcode tells you in which bucket you should look to find the key-value pair. 

*A hashcode is just an integer generated by an algorithm* that will always run the same way given a specific input, so the output will always be identical for any two identical inputs. 

For example, key is a string value then the a possible hashcode algorithm could be : You take the string and calculate an integer code from it by using A is 1, B is 2, and so on, adding the numeric values of all the letters in the string together. 

![Sorry. Image not loaded](./img/hashcode_example.png)

**Note:** Two different names might result in the same value. That's acceptable. The hashcode tells you only which bucket to go into and not how to locate the name once we're in that bucket.
You'll still have to search through the target bucket, reading each name until we find the desired name.

**In real-life hashing, it's common to have more than one entry in a bucket. Hashing retrieval is a two-step process.**   
**1. Find the right bucket (using hashCode()).**  
**2. Search the bucket for the right element (using equals())**  

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
**- Whenever it is invoked on the same object more than once during an execution of a Java application, the hashCode() method must consistently return the same integer, provided that no information used in equals() comparisons on the object is modified.**  

**- If two objects are equal according to the equals(Object) method, then calling the hashCode() method on each of the two objects must produce the same integer result.**  

**- It is NOT required that if two objects are unequal according to the equals(java.lang.Object) method, then calling the hashCode() method on each of the two objects must produce distinct integer results.**


![Sorry. Image not loaded](./img/hashcode_equals_table.png)

**What happens if you include a transient variable in your hashCode() method?**  
While that's legal (the compiler won't complain), under some circumstances, an object you put in a collection won't be found. As you might know, serialization saves an object so that it can be reanimated later by deserializing it back to full objectness. But transient variables are not saved when an object is serialized.

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
1. Give an object some state (assign values to its instance variables). Eg: x = 9, y = 7
2. Put the object in a HashMap, using the object as a key. 
3. Save the object to a file using serialization without altering any of its state. 
4. Retrieve the object from the file through deserialization. The object's transient variable will come back with a default value x==0 rather than the value the variable had at the time it was saved
5. Use the deserialized object to get the object out of the HashMap. So the new hashcode calculation will give a different hashcode and the equals() method fails as well since x is used to determine object equality.


**Bottom line: transient variables can really mess with your equals() and hashCode() implementations. Keep variables non-transient or, if they must be marked transient, don't use them to determine hashcodes or equality.**


### Collections
The Collections Framework in Java, gives you lists, sets, maps, and queues to satisfy most of your coding needs. 

**Basic operations you'll normally use with collections:**  
- Add objects to the collection. 
- Remove objects from the collection. 
- Find out if an object (or group of objects) is in the collection. 
- Retrieve an object from the collection without removing it. 
- Iterate through the collection, looking at each element (object) one after another.


**Key Interfaces and Classes of the Collections Framework**

**The core interfaces:**
![Sorry. Image not loaded](./img/interfaces.png)

**The concrete implementation classes:**
![Sorry. Image not loaded](./img/concrete_classes.png)


**There are really three overloaded uses of the word "collection":**  

- **collection (lowercase c)**, which represents any of the data structures in which objects are stored and iterated over. 

- **Collection (capital C)** is the java.util.Collection interface (with declarations of the methods common to most collections, including add(), remove(), contains(), size(), and iterator()) from which Set, List, and Queue extend. (That's right, extend, not implement. There are no direct implementations of Collection.) 

- **Collections (capital C and ends with s)** is the java.util.Collections class that holds a pile of static utility methods for use with collections.

![Sorry. Image not loaded](./img/interface_class_hierarchy.png)


**Not all collections in the Collections Framework actually implement the Collection interface. In other words, not all collections pass IS-A test for Collection.**  

**Specifically, none of the Map-related classes and interfaces extend from Collection. So while SortedMap, Hashtable, HashMap, TreeMap, and LinkedHashMap are all thought of as collections, none are actually extended from Collection-with-a capital-C.**  

**Collections come in four basic flavors:**  
**Lists** *Lists of things (classes that implement List)*  
**Sets** *Unique things (classes that implement Set)*  
**Maps** *Things with a unique ID (classes that implement Map)*  
**Queues** *Things arranged by the order in which they are to be processed*  

![Sorry. Image not loaded](./img/figure_11-3.png)


An implementation class can be unsorted and unordered, ordered but unsorted, or both ordered and sorted. But an implementation can never be sorted but unordered, because sorting is a specific type of ordering. For example, a HashSet is an unordered, unsorted set, while a LinkedHashSet is an ordered (but not sorted) set that maintains the order in which objects were inserted.

**Ordered**   
An ordered collection means you can iterate through the collection in a specific (not random) order.   
A Hashtable collection is not ordered. Although the Hashtable itself has internal logic to determine the order (based on hashcodes and the implementation of the collection itself), you won't find any order when you iterate through the Hashtable. 
An ArrayList, keeps the order established by the elements' index position (just like an array).  
LinkedHashSet keeps the order established by insertion (as opposed to an ArrayList, where you can insert an element at a specific index position). 

**Sorted**  
A sorted collection means that the collection keeps the elements in order determined according to some rule or rules, known as the sort order.  

Most commonly, the sort order used is the natural order.  
- For a collection of String objects, the natural order is alphabetical order. 
- For Integer objects, the natural order is by numeric value—1 before 2, and so on. 
- There is no natural order for custom objects unless the Foo developer provides one through an interface (Comparable or Comparator) that defines how instances of a class can be compared to one another.  
**NOTE:** sort order (including natural order) is not the same as ordering by insertion, access, or index.


**List Interface**  
A List cares about the index. The one thing that List has that non-lists don't is a set of methods related to the index like get(int index), indexOf(Object o), add(int index, Object obj), and so on. 

All three List implementations are ordered by index position—a position that you determine either by setting an object at a specific index or by adding it without specifying position, in which case the object is added to the end. 


**ArrayList**  
It’s a growable array & gives you fast iteration and fast random access. 
It is an ordered collection (by index), but not sorted. 
Choose this over a LinkedList when you need fast iteration but aren't as likely to be doing a lot of insertion and deletion. 

**Vector**   
A Vector is basically the same as an ArrayList, but Vector methods are synchronized for thread safety. You'll normally want to use ArrayList instead of Vector because the synchronized methods add a performance hit you might not need. And if you do need thread safety, there are utility methods in class Collections that can help. Vector is the only class other than ArrayList to implement RandomAccess.

**LinkedList**  
A LinkedList is ordered by index position, like ArrayList, except that the elements are doubly linked to one another. This linkage gives you new methods (beyond what you get from the List interface) for adding and removing from the beginning or end, which makes it an easy choice for implementing a stack or queue. Keep in mind that a LinkedList may iterate more slowly than an ArrayList, but it's a good choice when you need fast insertion and deletion. As of Java 5, the LinkedList class has been enhanced to implement the java.util. Queue interface & it now supports the common queue methods peek(), poll(), and offer().

**Set Interface**  
A Set cares about uniqueness—it doesn't allow duplicates. The equals() method determines whether two objects are identical (in which case, only one can be in the set). 

The three Set implementations are:  
**HashSet**  
A HashSet is an unsorted, unordered Set. It uses the hashcode of the object being inserted, so the more efficient your hashCode() implementation, the better access performance you'll get. 

**LinkedHashSet**  
A LinkedHashSet is an ordered version of HashSet that maintains the insertion order using  a doubly linked List across all elements. 

**When using HashSet or LinkedHashSet, the objects you add to them must override hashCode(). If they don't override hashCode(), the default Object.hashCode() method will allow multiple objects that you might consider "meaningfully equal" to be added to your "no duplicates allowed" set.**  

**TreeSet**  
The TreeSet is a sorted collection. It uses a Red-Black tree structure, and guarantees that the elements will be in ascending order, according to the natural order. Optionally TreeSet lets you define a custom sort order via a Comparator when you construct a TreeSet. As of Java 6, TreeSet implements NavigableSet.

**Map Interface**  
Lets you map a unique key (the ID) to a specific value, where both the key and the value are objects. The Map implementations let you do things like search for a value based on the key, ask for a collection of just the values, or ask for a collection of just the keys. 

Like Sets, Maps rely on the equals() method to determine whether two keys are the same or different.
	
**HashMap**  
The HashMap gives you an unsorted, unordered Map. Where the keys land in the Map is based on the key's hashcode, so, like HashSet, the more efficient your hashCode() implementation, the better access performance you'll get. HashMap allows one null key and multiple null values in a collection. 

**Hashtable**  
Just as Vector is a synchronized counterpart to the ArrayList, Hashtable is the synchronized counterpart to HashMap. Remember that you don't synchronize a class, so when we say that Vector and Hashtable are synchronized, we just mean that the key methods of the class are synchronized. 
Another difference, though, is that while HashMap lets you have null values as well as one null key, a Hashtable doesn't let you have anything that's null. 

**LinkedHashMap**   
Like its Set counterpart, LinkedHashSet, the LinkedHashMap collection maintains insertion order. Although it will be somewhat slower than HashMap for adding and removing elements, you can expect faster iteration with a LinkedHashMap. 

**TreeMap**   
A TreeMap is a sorted Map i.e sorted by the natural order of the elements by default. Like TreeSet, TreeMap lets you define a custom sort order via a Comparator when you construct a TreeMap. As of Java 6, TreeMap implements NavigableMap.

**Queue Interface**   
A Queue is designed to hold a list of things to be processed in some way. Queues support all of the standard Collection methods and they also have methods to add and subtract elements and review queue elements. 

**PriorityQueue**   
Since the LinkedList class has been enhanced to implement the Queue interface, basic queues can be handled with a LinkedList. The purpose of a PriorityQueue is to create a "priority-in, priority out" queue as opposed to a typical FIFO queue. A PriorityQueue's elements are ordered either by natural ordering (in which case the elements that are sorted first will be accessed first) or according to a Comparator. In either case, the elements' ordering represents their relative priority.

![Sorry. Image not loaded](./img/table_11-2.png)


### Using Collections 

## ArrayList Basics

Let's take a look at using an ArrayList that contains strings. In practice, you'll typically want to instantiate an ArrayList polymorphically, like this: 

```java
List myList = new ArrayList();
```

As of Java 5, you'll want to say: **List<String> myList = new ArrayList<String>()**  

This kind of declaration follows the object-oriented programming principle of "coding to an interface," and it makes use of generics. (Prior to Java 5, there was no way to specify the type of a collection, and when we cover generics)

In many ways, ArrayList is similar to a String[] in that it declares a container that can hold only strings but some of the advantages ArrayList has over arrays are
- It can grow dynamically. 
- It provides more powerful insertion and search mechanisms than arrays.

```java
List<String> test = new ArrayList<String>(); // declare the ArrayList
String s = "hi";
test.add("string"); // add some strings
test.add(s);
test.add(s+s);
System.out.println(test.size()); // use ArrayList methods
System.out.println(test.contains(42));
System.out.println(test.contains("hihi"));
test.remove("hi");
System.out.println(test.size());
	
//which produces
3
false
true
2
```
	
Notice that when we declared the ArrayList we didn't give it a size. 
We were able to ask the ArrayList for its size and whether it contained specific objects, we removed an object right out from the middle of it, and then we rechecked its size.

**Autoboxing with Collections**  
In general, collections can hold Objects but not primitives. 

Prior to Java 5, "wrapper classes" (e.g., Integer, Float, Boolean, and so on) were used to get primitives into and out of collections. 

With Java 5, primitives still have to be wrapped, but autoboxing takes care of it for you.

```java
//Prior to Java 5
List myInts = new ArrayList(); // pre Java 5 declaration
myInts.add(new Integer(42)); // Use Integer to "wrap" an int

//As of Java 5, we can say:
myInts.add(42); // autoboxing handles it!

```

In the old, pre–Java 5 days, if you wanted to make a wrapper, unwrap it, use it, and then rewrap it, you might do something like this:

```java
Integer y = new Integer(567); 		// make it
int x = y.intValue(); 			// unwrap it
x++; 					// use it
y = new Integer(x); 			// rewrap it
System.out.println("y = " + y); 	// print it
```

Now, with new and improved Java 5, you can say

```java
Integer y = new Integer(567); 		// make it
y++; 					// unwrap it, increment it,
 					// rewrap it
System.out.println("y = " + y); 	// print it
```

Both examples produce the following output:	y = 568  

The code appears to be using the postincrement operator on an object reference variable! But it's simply a convenience. Behind the scenes, the compiler does the unboxing and reassignment for you. Earlier, we mentioned that wrapper objects are immutable… this example appears to contradict that statement. It sure looks like y's value changed from 567 to 568. What actually happened, however, is that a second wrapper object was created and its value was set to 568.

**Proof:**  
```java
Integer y = 567; 			// make a wrapper
Integer x = y; 				// assign a second ref
 					// var to THE wrapper
System.out.println(y==x); 		// verify that they refer
 					// to the same object
y++; 					// unwrap, use, "rewrap"
System.out.println(x + " " + y); 	// print values
System.out.println(y==x); 		// verify that they refer
 					// to different objects

//Which produces the output:
true
 567 568
 false
 
//So, under the covers, when the compiler got to the line y++; it had to substitute something like this:
int x2 = y.intValue(); 			// unwrap it
x2++; 					// use it
y = new Integer(x2); 			// rewrap it
```

**How wrappers work with ==, !=, and equals() ?**  
The API developers decided that for all the wrapper classes, two objects are equal if they are of the same type and have the same value. It shouldn't be surprising that

```java
Integer i1 = 1000;
Integer i2 = 1000;
if(i1 != i2) System.out.println("different objects");
if(i1.equals(i2)) System.out.println("meaningfully equal");
```

produces the output
```
different objects
meaningfully equal
```

It's just two wrapper objects that happen to have the same value. Because they have the same int value, the equals() method considers them to be "meaningfully equivalent," and therefore returns true. 

**How about this one:**  
```java
Integer i3 = 10;
Integer i4 = 10;
if(i3 == i4) System.out.println("same object");
if(i3.equals(i4)) System.out.println("meaningfully equal");
```

This example produces the output:
```
same object
meaningfully equal
```

**Yikes! The equals() method seems to be working, but what happened with == and !=? Why is != telling us that i1 and i2 are different objects, when == is saying that i3 and i4 are the same object?**  

In order to save memory, two instances of the following wrapper objects (created through boxing) will always be == when their primitive values are the same:
- Boolean 
- Byte 
- Character from \u0000 to \u007f (7f is 127 in decimal) 
- Short and Integer from –128 to 127 

**The key to the answer is called object interning. Java interns small numbers (less than 128), so all instances of Integer(n) with n in the interned range are the same. Numbers greater than or equal to 128 are not interned, hence Integer(1000) objects are not equal to each other.**  

**When == is used to compare a primitive to a wrapper, the wrapper will be unwrapped and the comparison will be primitive to primitive.**  

**Where Boxing Can Be Used**  
It's common to use wrappers in conjunction with collections. Any time you want your collection to hold objects and primitives, you'll want to use wrappers to make those primitives collection-compatible.  

The general rule is that boxing and unboxing work wherever you can normally use a primitive or a wrapped object. The following code demonstrates some legal ways to use boxing

```java
class UseBoxing {
    public static void main(String[] args) {
        UseBoxing u = new UseBoxing();
        u.go(5);
    }
    boolean go(Integer i) { // boxes the int it was passed
        Boolean ifSo = true; // boxes the literal
        Short s = 300; // boxes the primitive
        if (ifSo) { // unboxing
            System.out.println(++s); // unboxes, increments, reboxes
        }
        return !ifSo; // unboxes, returns the inverse
    }
}
```

**Remember, wrapper reference variables can be null. That means you have to watch out for code that appears to be doing safe primitive operations but that could throw a NullPointerException:**  

```java
class Boxing2 {
    static Integer x;
    public static void main(String[] args) {
        doStuff(x);
    }
    static void doStuff(int z) {
        int z2 = 5;
        System.out.println(z2 + z);
    }
}
```

This code compiles fine, but the JVM throws a NullPointerException when it attempts to invoke doStuff(x) because x doesn't refer to an Integer object, so there's no value to unbox.


### The Java 7 "Diamond" Syntax
**Prior to Java 7, declaring type-safe collections before diamond syntax:**  
```java
ArrayList<String> stuff = new ArrayList<String>();
List<Dog> myDogs = new ArrayList<Dog>();
Map<String, Dog> dogMap = new HashMap<String, Dog>();
```

**As of Java 7, the type parameters duplicated in these declarations could be simplified to.**  
```java
ArrayList<String> stuff = new ArrayList<>();
List<Dog> myDogs = new ArrayList<>();
Map<String, Dog> dogMap = new HashMap<>();
```

**Notice that in the simpler Java 7 declarations, the right side of the declaration included the two characters "<>," which together make a diamond shape!**  






