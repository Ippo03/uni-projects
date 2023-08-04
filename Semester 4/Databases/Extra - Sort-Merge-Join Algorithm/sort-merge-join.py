def database_mergeJoin_(table1: list, table2: list, table1_join_index: int, table2_join_index: int):
    """JOIN TWO TABLES TABLE1 AND TABLE2 REPRESENTED AS A LIST, join as in the relational database context of join

    Args:
        table1 (list): First table as a list of rows, where each row is a list.
        table2 (list): Second table as a list of rows, where each row is a list.
        table1_join_index (int): The index of the column in table1 used for joining.
        table2_join_index (int): The index of the column in table2 used for joining.

    Returns:
        list: The result of the merge join as a list of rows, where each row is a list containing the joined data.

    Example:
        super_list1 = [
            [4, "a"],
            [1, "h"],
            [1, "z"],
            [2, "b"],
            [1, "c"],
            [3, "d"]
        ]
        super_list2 = [
            [4, "d"],
            [2, "f"],
            [1, "g"],
            [3, "h"]
        ]
        >>> database_mergeJoin_(super_list1, super_list2,0,0)
        [[1, 'h', 'g'], [1, 'z', 'g'], [1, 'c', 'g'], [2, 'b', 'f'], [3, 'd', 'h'], [4, 'a', 'd']]
        >>> database_mergeJoin_(super_list1, super_list2,0,1)
        []
    """
    result = []
    # Sort the two tables based on the specified join index
    sorted1 = sorted(table1, key=lambda x: x[table1_join_index])
    sorted2 = sorted(table2, key=lambda x: x[table2_join_index])
    
    index1, index2 = 0, 0
    
    # Merge join algorithm
    while index1 < len(table1) and index2 < len(table2):
        if sorted1[index1][table1_join_index] == sorted2[index2][table2_join_index]:
            tempindex1 = index1
            # If there are duplicates in table1, we need to check every possible combination
            # by moving at first index of table 2 and then table 1
            while tempindex1 < len(table1) and sorted1[tempindex1][table1_join_index] == sorted1[index1][table1_join_index]:
                tempindex2 = index2
                while tempindex2 < len(table2) and sorted2[tempindex2][table2_join_index] == sorted2[index2][table2_join_index]:
                    # Create a deep copy of the rows to avoid destroying the original data
                    temp1 = sorted1[tempindex1].copy()
                    temp2 = sorted2[tempindex2].copy()
                    
                    # Remove the join value from the second table to avoid including it twice
                    temp2.remove(sorted2[tempindex2][table2_join_index])

                    # Combine the rows and append to the result
                    temp1.extend(temp2)
                    result.append(temp1)
                    
                    tempindex2 += 1
                tempindex1 += 1
            index1 = tempindex1
            index2 = tempindex2
        elif str(sorted1[index1][table1_join_index]) < str(sorted2[index2][table2_join_index]):
            # Advance the pointer for table1 when its value is less than the one in table2
            index1 += 1
        else:
            # Advance the pointer for table2 when its value is less than the one in table1
            index2 += 1

    return result


