# -*- coding: utf-8 -*-


def read_iris(filename):
    '''
    récuperer les donées dans le fichier Iris.data. 

    @param filename: le nom du fichier où on récupère les données 

    '''
    
    f = open(filename)
    lines = f.readlines()
    data = []
    for i in xrange(len(lines)-1):   
        line = lines[i].split(',')
        line = [i+1]+[float(x) for x in line[:-1]]
        data.append(line)  
    f.close()
    
    return data

data= read_iris("iris.data")
print data


