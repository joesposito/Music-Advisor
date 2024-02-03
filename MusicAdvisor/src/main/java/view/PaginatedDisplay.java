package view;

import java.util.List;

// Responsible for the view of data
public class PaginatedDisplay<E> {
    private final List<E> list;
    private int currentIndex;
    private int currentPage;
    private final int numPages;

    // Given a list, we can create a series of pages to print
    public PaginatedDisplay(List<E> list){

        this.list = list;
        numPages = (int)Math.ceil(list.size()/(double)5);
        currentIndex = 0;

        if(list.isEmpty()){
            currentPage = 1;
        }else{
            currentPage = 0;
        }
    }

    // Prints the next page
    public void printNextPage() {
        if(currentPage >= numPages){
            System.out.println("No more pages.");
            return;
        }

        // Print the next 5 items
        List<E> elements = list.stream().skip(currentIndex).limit(5).toList();
        // Our next page will start at the index + 5
        currentIndex += 5;
        for(E e : elements){
            System.out.println(e);
            System.out.println();
        }
        currentPage++;
        pageCountPrint();
    }

    public void printPrevPage(){
        if(currentPage <= 1 || currentIndex - 10 < 0){
            System.out.println("No more pages.");
            return;
        }

        // Print the previous 5 items, must go back 10 in the index
        List<E> elements = list.stream().skip(currentIndex - 10).limit(5).toList();
        // Our next page will start at the index - 5
        currentIndex -= 5;
        for(E e : elements){
            System.out.println(e);
            System.out.println();
        }
        currentPage--;
        pageCountPrint();
    }


    // Just a helper method to print the page count
    public void pageCountPrint(){
        System.out.println("---PAGE " + currentPage + " OF " + numPages + "---");
    }
}
