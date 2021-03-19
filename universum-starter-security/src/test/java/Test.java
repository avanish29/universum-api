import io.jsonwebtoken.io.Encoders;

public class Test {

	public static void main(String[] args) {
		System.out.println(Encoders.BASE64.encode("$2y$10$GBIQaf6gEeU9im8RTKhIgOZ5q5haDA.A5GzocSr5CR.sU8OUsCUwq".getBytes()));
	}

}
