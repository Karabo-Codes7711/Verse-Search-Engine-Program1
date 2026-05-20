import java.io.File;
import java.util.Scanner;

class Program{

  public static int wordCount = 0;
  public static int indexCount = 0;

  public static void main(String[] args) throws Exception {
    WordLocation[] words = new WordLocation[10000];

    for(int i = 1; i <= 101; i++) {
      String fileName = "Verse" + i + ".txt";

      File file = new File(fileName);

      try(Scanner reader = new Scanner(file)){
        while (reader.hasNextLine()) {
          String line = reader.nextLine();

          line = line.replaceAll("[^a-zA-Z ]", "");
          line = line.toLowerCase();

          String[] split = line.split(" ");

          for (int j = 0; j < split.length; j++) {
            if (!split[j].equals("")) {
              words[wordCount] = new WordLocation(split[j], i);
              wordCount++;
            }
          }
        }
      }catch(Exception e){
        System.out.println("Error reading file");
      }
    }

    WordList[] index = new WordList[5000];
    mergeSort(words,0,wordCount - 1);

    for(int i = 0; i < wordCount; i++)
    {
      String currentWord = words[i].word;

      int pos = binarySearchRecursive(index, currentWord, 0, indexCount - 1);

      if(pos == -1)
      {
        index[indexCount] = new WordList(currentWord);

        index[indexCount].head = insertSorted(index[indexCount].head, words[i].location);

        indexCount++;
      }
      else
      {
        index[pos].head = insertSorted(index[pos].head, words[i].location);
      }
    }



    Scanner sc = new Scanner(System.in);

    while(true){
      System.out.println("Enter word: ");
      String word = sc.nextLine().toLowerCase();

      if(word.equals("")){
        break;
      }

      Node result = null;

      String[] searchWords = word.split(" ");

      for(int i = 0; i < searchWords.length; i++)
      {
        int pos = binarySearchRecursive(index, searchWords[i], 0, indexCount - 1);

        if(pos == -1)
        {
          result = null;
          break;
        }

        if(i == 0)
        {
          result = index[pos].head;
        }
        else
        {
          result = intersectionMerge(result, index[pos].head);
        }
      }
      System.out.println();
      System.out.println("Results:");
      System.out.println();

      displayVerses(result);
    }
  }
  public static void displayVerses(Node head) throws Exception
  {
    while(head != null)
    {
      String fileName = "Verse" + head.cargo + ".txt";

      File file = new File(fileName);

      Scanner scan = new Scanner(file);

      System.out.println("Verse " + head.cargo);

      while(scan.hasNextLine())
      {
        System.out.println(scan.nextLine());
      }

      System.out.println();

      head = head.next;
    }
  }

  public static Node insertSorted(Node curHead, int value)
  {
    if(curHead == null)
    {
      return new Node(value);
    }

    if(value < curHead.cargo)
    {
      Node temp = new Node(value);
      temp.next = curHead;
      return temp;
    }

    if(value == curHead.cargo)
    {
      return curHead;
    }

    curHead.next = insertSorted(curHead.next, value);

    return curHead;
  }

  public static Node intersectionMerge( Node head1, Node head2) {
    Node result = null;

    while (head1 != null && head2 != null) {
      if (head1.cargo == head2.cargo) {
        result = insertSorted(result, head1.cargo);
        head1 = head1.next;
        head2 = head2.next;
      }
      else if (head1.cargo < head2.cargo) {
        head1 = head1.next;
      }
      else {
        head2 = head2.next;
      }
    }
    return result;
  }

  public static int binarySearchRecursive(WordList[] list,String word,int min,int max){
    if(min > max){
      return -1;
    }
    int mid = (min + max)/2;



    if(list[mid] == null){
      return -1;
    }

    int results = word.compareTo(list[mid].word);

    if(results == 0){
      return mid;
    }

    if(results < 0) {
      return binarySearchRecursive(list, word, min, mid - 1);
    }

    return  binarySearchRecursive(list, word, mid + 1, max);
  }

  public static void mergeSort(WordLocation[] arr, int left, int right)
  {
    if(left < right)
    {
      int mid = (left + right) / 2;

      mergeSort(arr, left, mid);
      mergeSort(arr, mid + 1, right);

      merge(arr, left, mid, right);
    }
  }



  public static void merge(WordLocation[] arr, int left, int mid, int right){
    int size1 =  mid - left + 1;
    int size2 = right - mid;

    WordLocation[] List1 = new WordLocation[size1];
    WordLocation[] List2 = new WordLocation[size2];

    for(int i = 0; i < size1; i++){
      List1[i] = arr[left + i];
    }

    for(int j = 0; j < size2; j++){
      List2[j] = arr[mid + 1 + j];
    }

    int i = 0;
    int j = 0;
    int k = left;

    while(i < size1 && j < size2)
    {
      if(List1[i].word.compareTo(List2[j].word) <= 0)
      {
        arr[k] = List1[i];
        i++;
      }
      else
      {
        arr[k] = List2[j];
        j++;
      }
      k++;
    }

    while(i < size1)
    {
      arr[k] = List1[i];
      i++;
      k++;
    }

    while(j < size2)
    {
      arr[k] = List2[j];
      j++;
      k++;
    }
  }
}
