package hiphip.int_;

import clojure.lang.IFn;

public class JavaBaseline {
  /*********************************************************************************************
   * Functions equivalent to (specific applications of) hiphip macros for benchmarking and
   * testing purposes.
   *********************************************************************************************/

  public static int alength(int [] arr) {
    return arr.length;
  }

  public static int aget(int [] arr, int idx) {
    return arr[idx];
  }

  public static int aset(int [] arr, int idx, int v) {
    arr[idx] = v;
    return v;
  }

  public static int ainc(int [] arr, int idx, int v) {
    return arr[idx]+=v;
  }
  
  public static int[] aclone(int [] arr) {
    return arr.clone();
  }
  
  // tests areduce and dot-product 
  public static int dot_product(int[] arr1, int[] arr2) {
    int s = 0;
    for (int i = 0; i < arr1.length; i++) {
      s += arr1[i] * arr2[i];
    }
    return s;
  }

  
  // tests doarr and afill!
  public static int[] multiply_in_place_pointwise(int[] xs, int[] ys) {
    for(int i = 0; i < xs.length; i++) {
      xs[i] *= ys[i];
    }
    return xs;
  }

  // tests afill!
  public static int[] multiply_in_place_by_idx(int[] xs) {
    for(int i = 0; i < xs.length; i++) {
      xs[i] *= i;
    }
    return xs;
  }

  // tests amake
  public static int[] acopy_inc(int len, int[] ys) {
    int[] ret = new int[len];
    for(int i = 0; i < len; i++) {
      ret[i] = ys[i] + 1;
    }
    return ret;
  }

  public static int[] amap_inc(int[] arr) {
    int[] ret = new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
      ret[i] = arr[i] + 1;
    }
    return ret;
  }

  public static int[] amap_plus_idx(int[] arr) {
    int[] newarr = new int[arr.length];
    for (int i = 0; i < arr.length; i++) {
      newarr[i] = arr[i] + i;
    }
    return newarr;
  }

  public static int asum(int[] arr) {
    int s = 0;
    for (int d : arr) {
      s += d;
    }
    return s;
  }

  public static int asum_square(int[] arr) {
    int s = 0;
    for (int d : arr) {
      s += d * d;
    }
    return s;
  }

  public static int aproduct(int[] arr) {
    int s = 1;
    for (int d : arr) {
      s *= d;
    }
    return s;
  }

  public static int amax(int[] arr) {
    int m = arr[0];
    for (int i = 1; i < arr.length; i++) {
      int v = arr[i];
      if (v > m) m = v;
    }
    return m;
  }

  public static int amin(int[] arr) {
    int m = arr[0];
    for (int i = 1; i < arr.length; i++) {
      int v = arr[i];
      if (v < m) m = v;
    }
    return m;
  }

  public static int amean(int[] arr) {
    return asum(arr) / arr.length;
  }
 
  /*********************************************************************************************
   * Functions used within hiphip API, since we couldn't (yet) generate pure Clojure versions 
   * that are (close to) as efficient as Java.
   *********************************************************************************************/

  /**
  * Returns the first index of a largest value in xs, which must have nonzero length.
  * 
  * @param xs the array
  * @return   the first index of a maximum value in xs
  */    
  public static int maxIndex(int[] xs) {
    int am = 0;
    int m = xs[0];
    for (int i=1; i < xs.length; ++i) {
      int v = xs[i];
      if (v > m) {
	m = v;
	am = i;
      }
    }
    return am;
  }

  /**
  * Returns the first index of a smallest value in xs, which must have nonzero length.
  * 
  * @param xs the array
  * @return   the first index of a minimum value in xs
  */    
  public static int minIndex(int[] xs) {
    int am = 0;
    int m = xs[0];
    for (int i=1; i < xs.length; ++i) {
      int v = xs[i];
      if (v < m) {
	m = v;
	am = i;
      }
    }
    return am;
  }

  private static void swap(int[] arr, int i, int j) {
    int tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }
  
  /**
  * Partitions an array using a standard 3-way partitioning algorithm.  Given an array 
  * arr, a range in this array [left, right), and a pivot, modifies arr so that all 
  * elements less than pivot come first (in no particular order), followed by all equal
  * elements, followed by all greater elements.  
  * 
  * @param  arr   the array to be partitioned
  * @param  left  the index to start partitioning at 
  * @param  right the index to stop partitioning at
  * @param  pivot the value to partition by 
  * @return       the 1 + the greatest index less than or equal to pivot.
  */    
  public static int partition(int[] arr, int left, int right, int pivot) {
    int i = left,  // right of last element known less than pivot
        j = right;   // first element known greater than pivot
    for (int k = i; k < j; k++) {
      while (pivot < arr[k]) {
        j--;
        if (j == k) return j;
        swap(arr, j, k);
      }
      if (arr[k] < pivot) {
        if (i < k) {
          swap(arr, i, k);
        }
        i++;
      }
    }
    return j;
  }

  private static int choosePivot(int[] arr, int left, int right) {
    return arr[(left+right)/2];
  }
  
  /**
  * Selects the top k elements of an array using a quickselect algorithm.  Given an array 
  * arr, a range in this array [left, right), and an int k, modifies arr so that the  
  * smallest k elements come first, followed by all ties for k, followed by all greater
  * elements.
  * 
  * @param  arr   the array to be selected
  * @param  left  the index to start selecting at 
  * @param  right the index to stop selecting at
  * @param  k     the number of elements to select
  */    
  public static void select(int[] arr, int left, int right, int k) {
    int pivot = choosePivot(arr, left, right);
    int part = partition(arr, left, right, pivot);
            
    if (part < k) {
      select(arr, part, right, k - part);
    } else if (k < part) {
      part--;
      // Skip over all elements equal to pivot
      assert pivot == arr[part];
      do {
        part--;
        if (part < k) return;
      } while(arr[part] == pivot);
      select(arr, left, part+1, k);
    }
  }
  
  
  /**
  * Partitions an array using a standard 3-way partitioning algorithm.  Given an array 
  * arr, an array of indices into this array, a range of indices into this array 
  * [left, right), and a pivot, modifies indices so that all elements pointing at arr
  * elements less than pivot come first (in no particular order), followed by all equal
  * elements, followed by all greater elements.  
  * 
  * @param  indices indices into the array to be partitioned
  * @param  arr     the array to be partitioned
  * @param  left    the index to start partitioning at 
  * @param  right   the index to stop partitioning at
  * @param  pivot   the value to partition by 
  * @return         the 1 + the greatest index less than or equal to pivot.
  */    
  public static int partitionIndices(int [] indices, int[] arr, int left, int right, int pivot) {
    int i = left,  // right of last element known less than pivot
        j = right;   // first element known greater than pivot
    for (int k = i; k < j; k++) {
      while (pivot < arr[indices[k]]) {
        j--;
        if (j == k) return j;        
        hiphip.IndexArrays.swap(indices, j, k);
      }
      if (arr[indices[k]] < pivot) {
        if (i < k) {
          hiphip.IndexArrays.swap(indices, i, k);
        }
        i++;
      }
    }
    return j;
  }
  
  private static int choosePivot(int[] indices, int[] arr, int left, int right) {
    return arr[indices[(left+right)/2]];
  }
  
 /**
  * Selects the top k indices of an array using a quickselect algorithm.  Given an array 
  * arr, an array of indices into this array, a range in indices [left, right), and  
  * an int k, modifies indices so that the indices pointing at the smallest k elements 
  * come first, followed by all ties for k, followed by all greater elements.
  * 
  * @param  indices indices into the array to be selected
  * @param  arr     the array to be selected
  * @param  left    the index to start selecting at 
  * @param  right   the index to stop selecting at
  * @param  k       the number of elements to select
  */    
  public static void selectIndices(int[] indices, int[] arr, int left, int right, int k) {
    int pivot = choosePivot(indices, arr, left, right);
    int part = partitionIndices(indices, arr, left, right, pivot);
    if (part < k) {
      selectIndices(indices, arr, part, right, k - part);
    } else if (k < part) {
      part--;
      // Skip over all elements equal to pivot
      assert pivot == arr[indices[part]];
      do {
        part--;
        if (part < k) return;
      } while(arr[indices[part]] == pivot);
      selectIndices(indices, arr, left, part+1, k);
    }
  }

 /**
  * Sorts the indices of an array using a quicksort algorithm.  Given an array 
  * arr, an array of indices into this array, and a range in indices [left, right),   
  * modifies indices so that the first index points at the smallest element of arr,
  * and so on.
  * 
  * @param  indices indices into the array to be sorted
  * @param  arr     the array to be sorted
  * @param  left    the index to start sorting at 
  * @param  right   the index to stop sorting at
  */    
  public static void sortIndices(int[] indices, int[] arr, int left, int right) {
    int pivot = choosePivot(indices, arr, left, right);
    int part = partitionIndices(indices, arr, left, right, pivot);
    if (part+1 < right) sortIndices(indices, arr, part, right);
    part--;
    // Skip over all elements equal to pivot
    assert pivot == arr[indices[part]];
    do {
      part--;
      if (part <= left) return;
    } while(arr[indices[part]] == pivot);
    sortIndices(indices, arr, left, part+1);
  }
}
