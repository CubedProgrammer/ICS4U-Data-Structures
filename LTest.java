import static java.lang.System.out;
public class LTest {

    public static final void main(String[]args) {

        LList<Integer>lis=new LList<Integer>();

        lis.push(1);
        lis.push(2);
        lis.push(3);
        out.println(lis);

        while(lis.size()>0)
            out.println(lis.pop());

        lis.enqueue(1);
        lis.enqueue(2);
        lis.enqueue(3);
        out.println(lis);

        while(lis.size()>0)
            out.println(lis.dequeue());

        lis.enqueue(1);
        lis.enqueue(2);
        lis.enqueue(3);
        lis.enqueue(4);
        lis.enqueue(5);
        lis.enqueue(6);

        lis.reverse();
        out.println(lis);

        lis.delete(6);
        lis.deleteAt(2);
        out.println(lis);

        lis.sortedInsert((x,y)->x-y,3);
        out.println(lis);
        lis.sortedInsert((x,y)->x-y,0);
        out.println(lis);
        lis.sortedInsert((x,y)->x-y,9);
        out.println(lis);

        lis.enqueue(1);
        lis.enqueue(2);
        lis.enqueue(3);
        lis.enqueue(4);
        lis.enqueue(5);
        lis.enqueue(6);
        out.println(lis);
        lis.removeDuplicates();
        out.println(lis);
        out.println(lis.clone());

    }

}