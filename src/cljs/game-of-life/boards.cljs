(ns game-of-life.boards)

(def uncle-buck
  {:width 30
   :height 30
   :grid
     #{[0  0] [1  0] [2  0] [3  0] [4  0] [5  0] [6  0] [7  0] [8  0] [9  0] [10  0] [11  0] [12  0] [13  0] [14  0] [15  0] [16  0] [17  0] [18  0] [19  0] [20  0] [21  0] [22  0] [23  0] [24  0] [25  0] [26  0] [27  0] [28  0] [29  0]
       [0  1] [1  1] [2  1] [3  1] [4  1] [5  1] [6  1] [7  1] [8  1] [9  1] [10  1] [11  1] [12  1] [13  1] [14  1] [15  1] [16  1] [17  1] [18  1] [19  1] [20  1] [21  1] [22  1] [23  1] [24  1] [25  1] [26  1] [27  1] [28  1] [29  1]
       [0  2] [1  2] [2  2] [3  2] [4  2] [5  2]                      [9  2] [10  2] [11  2] [12  2] [13  2] [14  2] [15  2] [16  2] [17  2] [18  2] [19  2] [20  2] [21  2] [22  2] [23  2] [24  2] [25  2] [26  2] [27  2] [28  2] [29  2]
       [0  3] [1  3] [2  3] [3  3] [4  3]               [7  3] [8  3] [9  3] [10  3] [11  3]         [13  3] [14  3] [15  3] [16  3] [17  3] [18  3] [19  3] [20  3] [21  3] [22  3]                 [25  3] [26  3] [27  3] [28  3] [29  3]
       [0  4] [1  4] [2  4] [3  4]                      [7  4]               [10  4] [11  4]                         [15  4] [16  4] [17  4]                 [20  4] [21  4] [22  4]                                 [27  4] [28  4] [29  4]
       [0  5] [1  5] [2  5]                                           [9  5] [10  5] [11  5] [12  5]                 [15  5] [16  5]         [18  5] [19  5] [20  5] [21  5] [22  5] [23  5] [24  5]                 [27  5] [28  5] [29  5]
       [0  6] [1  6] [2  6]                                                  [10  6] [11  6] [12  6] [13  6]                                 [18  6]         [20  6] [21  6] [22  6]         [24  6]                 [27  6] [28  6] [29  6]
       [0  7] [1  7]                                                                         [12  7] [13  7]                                                         [21  7]                         [25  7] [26  7] [27  7] [28  7] [29  7]
              [1  8]                                                                                                                                                                                         [26  8] [27  8] [28  8] [29  8]
              [1  9]                                                                                                                                                                                                 [27  9] [28  9] [29  9]
                                                                                                                                                                                                                             [28 10] [29 10]
       [0 11] [1 11]                                                         [10 11] [11 11]                                                                                                                         [27 11] [28 11] [29 11]
       [0 12]                                                         [9 12] [10 12]                                                                                                                                 [27 12] [28 12]
       [0 13]                                                  [8 13] [9 13] [10 13] [11 13] [12 13] [13 13] [14 13]         [16 13]         [18 13] [19 13]                                                         [27 13] [28 13]
                                                        [7 14] [8 14]                        [12 14] [13 14] [14 14] [15 14] [16 14] [17 14] [18 14] [19 14] [20 14]                                                 [27 14] [28 14] [29 14]
                                          [5 15] [6 15] [7 15]                                       [13 15] [14 15] [15 15] [16 15] [17 15]                 [20 15] [21 15]                                         [27 15]
       [0 16]                                    [6 16]        [8 16]        [10 16]                         [14 16] [15 16]                                 [20 16] [21 16] [22 16]         [24 16]         [26 16]
       [0 17]                                                  [8 17] [9 17] [10 17] [11 17] [12 17] [13 17] [14 17] [15 17] [16 17]                                 [21 17] [22 17] [23 17] [24 17]         [26 17]
       [0 18]                                                                [10 18]                                 [15 18] [16 18] [17 18] [18 18] [19 18] [20 18] [21 18] [22 18] [23 18] [24 18]         [26 18]
       [0 19]                                                                        [11 19] [12 19] [13 19] [14 19] [15 19] [16 19] [17 19] [18 19] [19 19] [20 19]                 [23 19] [24 19]         [26 19]
       [0 20] [1 20]                                                                                 [13 20] [14 20] [15 20] [16 20] [17 20] [18 20] [19 20] [20 20]                 [23 20]         [25 20]
       [0 21] [1 21]                                                                         [12 21]                                         [18 21] [19 21] [20 21]         [22 21] [23 21] [24 21] [25 21]
       [0 22] [1 22] [2 22]                                                                  [12 22] [13 22] [14 22] [15 22] [16 22] [17 22] [18 22] [19 22] [20 22] [21 22] [22 22] [23 22] [24 22] [25 22]
       [0 23] [1 23] [2 23] [3 23]                                                                   [13 23] [14 23] [15 23] [16 23]         [18 23] [19 23]         [21 23] [22 23]         [24 23] [25 23] [26 23] [27 23] [28 23]
       [0 24] [1 24] [2 24] [3 24] [4 24]               [7 24]                                                                               [18 24] [19 24] [20 24] [21 24]         [23 24] [24 24] [25 24] [26 24] [27 24]         [29 24]
              [1 25] [2 25] [3 25] [4 25] [5 25]        [7 25] [8 25]                                                                        [18 25] [19 25] [20 25]         [22 25] [23 25] [24 25] [25 25] [26 25] [27 25] [28 25]
       [0 26] [1 26] [2 26] [3 26] [4 26] [5 26] [6 26]        [8 26] [9 26]                                                         [17 26] [18 26] [19 26] [20 26] [21 26] [22 26] [23 26] [24 26] [25 26] [26 26] [27 26] [28 26]
       [0 27] [1 27] [2 27] [3 27] [4 27] [5 27] [6 27] [7 27] [8 27] [9 27] [10 27] [11 27]                                 [16 27] [17 27] [18 27] [19 27] [20 27] [21 27] [22 27] [23 27] [24 27] [25 27] [26 27] [27 27] [28 27] [29 27]
       [0 28] [1 28] [2 28] [3 28] [4 28] [5 28] [6 28] [7 28] [8 28] [9 28] [10 28] [11 28] [12 28] [13 28] [14 28] [15 28] [16 28] [17 28] [18 28] [19 28] [20 28] [21 28] [22 28] [23 28]         [25 28] [26 28] [27 28] [28 28] [29 28]
       [0 29] [1 29] [2 29] [3 29] [4 29] [5 29] [6 29] [7 29] [8 29] [9 29] [10 29] [11 29] [12 29] [13 29] [14 29] [15 29] [16 29] [17 29] [18 29] [19 29] [20 29] [21 29] [22 29] [23 29] [24 29] [25 29] [26 29] [27 29] [28 29] [29 29]}})
