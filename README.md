# Vernam
 A class used to encrypt and decrypt text specified in .txt file with the vernam cipher

---

##Usage

To init vernam algorithm create Vernam object. As a parameter you can set path to **.txt** file. As default this file is added in project directory.

        Vernam vernam = new Vernam(); //with default file directory
        
        Vernam vernam = new Vernam("path to file"); // with own path to file  
        
Call **encryptBytes()** method to get array of encrypted bits. You could call **createTextFromBitsArray(byte[] param)** to convert it to string.

        byte[] encryptedArray = vernam.encryptBytes();
        System.out.println("Encrypted Message: \n" + vernam.createTextFromBitsArray(encryptedArray) + "\n");

Call **decryptBytes(byte[] param)** method to get array of decrypted bits. You could call **createTextFromBiitsArray(byte[] param)** to get decrypted string that should be exactly like enterd  text.

        byte[] decryptedArray = vernam.decryptBytes(encryptedArray);
        System.out.println("Decrypted Message: \n" + vernam.createTextFromBitsArray(decryptedArray));