class PlayFairCipher{
    public char[][] matrix;
    public String key;

    // Constructor
    PlayFairCipher(String key){
        this.key = key;

        matrix = new char[5][5];
        key = key.toUpperCase().replace("J","I");
        StringBuilder processed = new StringBuilder();
        boolean[] used = new boolean[26];
        for (char c : key.toCharArray()){
            if (Character.isLetter(c)){ // Check that it is an alphabet
                if(!used[c-'A']){
                    used[c-'A']=true;
                    processed.append(c);
                }
            }
        }

        // Add remaining alphabets
        for(char c='A'; c<='Z'; c++){
            if(c=='J'){  // Skip J
                continue;
            }
            if(!used[c-'A']){
                used[c-'A'] = true;
                processed.append(c);
            }
        }

        // Fill the matrix
        int index = 0;
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                matrix[i][j] = processed.charAt(index);
                index++;
            }
        }
    }

    // PlainText
    public String[] plainText(String text){
        text = text.toUpperCase().replace("J","I").replaceAll("[^A-Z]", "");  // Replace J with I and removw everything that is not an alphabet
        StringBuilder processed = new StringBuilder();

        // Check the chars in text
        for(int i=0; i<text.length(); i++){
            char a = text.charAt(i);
            char b ;
            if(i<text.length()-1){
                b = text.charAt(i+1);

                if(a==b){  // Same letters in the same pair
                    processed.append(a).append('X');
                }
                else{  // Normal pair
                    processed.append(a).append(b);
                    i++;
                }
            }
            else{  // Last char if odd
                processed.append(a).append('X');
            }
        }

        // Creating pairs
        StringBuilder paired = new StringBuilder();
        char[] arr = processed.toString().toCharArray();
        for(int i=0; i<processed.length(); i+=2){
            paired.append(arr[i]).append(arr[i+1]).append(" ");
        }

        return new String[] { processed.toString(), paired.toString() };
    }

    // Find position in matrix
    public int[] findPosition(char c){
        int[] pos = new int[2];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if (c==matrix[i][j]){
                    pos[0]=i;
                    pos[1]=j;
                     // Returns an array with 2 elements (row, column)
                }
            }
        }
        return pos;  // If char does not exist
    }

    // Print Matrix
    public void printMatrix(){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                System.out.print(" | " + matrix[i][j] );
            }
            System.out.print(" |");  
            System.out.println();
        }
    }

    // --------------- Encryption
    public String[] encrypt(String text){
        String[] plaintext = plainText(text);      
        //char[][] matrix = generateMatrix(); 
        StringBuilder ciphertext = new StringBuilder();

        for(int i=0; i<plaintext[0].length(); i += 2){  // Using pairs
            char a = plaintext[0].charAt(i);
            char b = plaintext[0].charAt(i+1);

            int[] pos_a = findPosition(a);
            int[] pos_b = findPosition(b);

            // In the same row
            if(pos_a[0] == pos_b[0]){
                char en_a = matrix[pos_a[0]][(pos_a[1]+1)%5];
                char en_b = matrix[pos_b[0]][(pos_b[1]+1)%5];

                ciphertext.append(en_a).append(en_b);
            }
            // In the same column
            else if(pos_a[1] == pos_b[1]){
                char en_a = matrix[(pos_a[0]+1)%5][pos_a[1]];
                char en_b = matrix[(pos_b[0]+1)%5][pos_b[1]];

                ciphertext.append(en_a).append(en_b);
            }
            else{
                char en_a = matrix[pos_a[0]][pos_b[1]];   // Row of same char, but column of the other char
                char en_b = matrix[pos_b[0]][pos_a[1]];

                ciphertext.append(en_a).append(en_b);
            }
        }
        // Creating pairs
        StringBuilder pairedcipher = new StringBuilder();
        for(int i=0; i<ciphertext.length(); i+=2){
            char a = ciphertext.charAt(i);
            char b = ciphertext.charAt(i+1);
            pairedcipher.append(a).append(b).append(" ");
        }
        return new String[] { ciphertext.toString(), pairedcipher.toString()} ;
    }

    // ---------------- Decryption
    public String[] decrypt(String encrypted){
        //char[][] matrix = generateMatrix(); 
        StringBuilder decrypted = new StringBuilder();

        for(int i=0; i<encrypted.length(); i += 2){  // Using pairs
            char a = encrypted.charAt(i);
            char b = encrypted.charAt(i+1);

            int[] pos_a = findPosition(a);
            int[] pos_b = findPosition(b);

            // In the same row
            if(pos_a[0] == pos_b[0]){
                char en_a = matrix[pos_a[0]][(pos_a[1]+4)%5];
                char en_b = matrix[pos_b[0]][(pos_b[1]+4)%5];

                decrypted.append(en_a).append(en_b);
            }
            // In the same column
            else if(pos_a[1] == pos_b[1]){
                char en_a = matrix[(pos_a[0]+4)%5][pos_a[1]];
                char en_b = matrix[(pos_b[0]+4)%5][pos_b[1]];

                decrypted.append(en_a).append(en_b);
            }
            else{
                char en_a = matrix[pos_a[0]][pos_b[1]];   // Row of same char, but column of the other char
                char en_b = matrix[pos_b[0]][pos_a[1]];

                decrypted.append(en_a).append(en_b);
            }
        }

        // Creating pairs
        StringBuilder pairedcipher = new StringBuilder();
        for(int i=0; i<decrypted.length(); i+=2){
            char a = decrypted.charAt(i);
            char b = decrypted.charAt(i+1);
            pairedcipher.append(a).append(b).append(" ");
        }

        return new String[] {decrypted.toString(), pairedcipher.toString()} ;
    }
}

public class Task1{
    public static void main(String[] args){
        PlayFairCipher cipher = new PlayFairCipher("explanation");
        String text = "Information security is a MUST to learn";

        System.out.println("-------------------------");
        cipher.printMatrix();
        System.out.println("-------------------------");

        String[] paired = cipher.plainText(text);
        System.out.println("---------------------------------------------------------------");
        System.out.println("Plain Text: " + paired[0]);

        System.out.println("---------------------------------------------------------------");
        System.out.println("Paired: " + paired[1]);
        
        String[] encrypted = cipher.encrypt(paired[0]);
        String[] decrypted = cipher.decrypt(encrypted[0]);
        
        System.out.println("---------------------------------------------------------------");
        System.out.println("Encrypted: " + encrypted[1]);
        System.out.println("---------------------------------------------------------------");
        System.out.println("Decrypted: " + decrypted[1]);
        System.out.println("---------------------------------------------------------------");
    }
}
