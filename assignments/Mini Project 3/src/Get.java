import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Anders on 03/11/14.
 */
public class Get {
    public Get(int keyToGetFrom, int portToGetFrom, int portToReceiveAt) {
        System.out.println(get(keyToGetFrom, portToGetFrom, portToReceiveAt));
    }

    private String get(int messageKey, int portToGetFrom, int portToReceiveTo) {
        throw new NotImplementedException();
    }
}
