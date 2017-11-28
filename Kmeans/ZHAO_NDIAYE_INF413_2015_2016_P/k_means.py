# -*- coding: utf-8 -*-

import random
import sys
from math import sqrt
import numpy as np
from matplotlib import pyplot as plt
from mpl_toolkits.mplot3d import Axes3D



def read_data(filename, skip_first_line = False, ignore_first_column = False):
    '''
    Loads data from a csv file and returns the corresponding list.
    All data are expected to be floats, except in the first column.
    
    @param filename: csv file name.
    
    @param skip_first_line: if True, the first line is not read.
        Default value: False.
    
    @param ignore_first_column: if True, the first column is ignored.
        Default value: False.
    
    @return: a list of lists, each list being a row in the data file.
        Rows are returned in the same order as in the file.
        They contains floats, except for the 1st element which is a string
        when the first column is not ignored.
    '''
    
    f = open(filename,'r')
    if skip_first_line:
        f.readline()
    
    data = []
    for line in f:
        line = line.split(",")
        line[1:] = [ float(x) for x in line[1:] ]
        if ignore_first_column:
            line = line[1:]
        data.append(line)
    f.close()
    return data

def write_data(data, filename):
    '''
    Writes data in a csv file.

    @param data: a list of lists

    @param filename: the path of the file in which data is written.
        The file is created if necessary; if it exists, it is overwritten.
    '''
    # If you're curious, look at python's module csv instead, which offers
    # more powerful means to write (and read!) csv files.
    f = open(filename, 'w')
    for item in data:
        f.write(','.join([repr(x) for x in item]))
        f.write('\n')
    f.close()

def generate_random_data(nb_objs, nb_attrs, frand = random.random):
    '''
    Generates a matrix of random data.
    
    @param frand: the fonction used to generate random values.
        It defaults to random.random.
        Example::

            import random
            generate_random_data(5, 6, lambda: random.gauss(0, 1))
    
    @return: a matrix with nb_objs rows and nb_attrs+1 columns. The 1st
        column is filled with line numbers (integers, from 1 to nb_objs).
    '''
    data = []
    for i in range(nb_objs):
        #line = [i+1]
        #for j in range(numAtt):
        #    line.append(frand())
        line = [i+1] + map(lambda x: frand(), range(nb_attrs))
        data.append(line)
    return(data)
        

###################################################################################################################################### 


def generate_random_centres(k,data):
    '''
    cree k centres avec une fonction aleatoire

    @param k: le nombre de centres que l'on veut generer aleatoirement

    @param data: le tableau de donnees d'entrees sur lequel on prend un quelques centres aleatoirement

    @return: un liste de centres pris dans la tableau de donnees

    '''
    #on verirfie si le nombre de centres k ne depasse pas la taille data, si c'est  le ca on renvoie erreur
    if k<len(data):
        centres=[]
        #on recupere aleatoirement k centres dans data avec la fonction rand.sample
        centres=random.sample(data,k)
        return(centres)
    else:
        return False

###################################################################################################################################### 

def calcul_distance(centres, data):
    '''
    calcule la distance entre les observations et les centres designes

    @param centres: la liste des centres 

    @param data: le tableau de donnees d'entrees sur lequel on calcule la distance avec les centres

    @return: une liste de distances calculées avec pour premier element le numero d'observation

    '''
    dist=[]
    for i in range(0,len(data)):
        dist.append([])
        #on met les numeros d'observation avant de faire le calcul de distance
        dist[i].append(data[i][0])
        for j in range(0,len(centres)):
            som=0
            #pour chaque observation, on calcule la distance correspondante avec chaque centre et on l'ajoute dans notre dans la liste d'indice i
            for k in range (1, len(data[i])):
                som+= (data[i][k]-centres[j][k])**2
            dist[i].append(sqrt(som))
            
    return dist

###################################################################################################################################### 


def affectation(distance,centre,data):
    '''
    cree les affectations correspondantes a chaque observation

    @param centre: la liste des centres 

    @param data: le tableau de donnees d'entrees

    @param distance: la liste des distances calculees pour chaque observation

    @return: une liste des donnees d'entree et leur observation correspondante a la fin

    '''
    
    affect=[]
    for i in range(0,len(distance)):
        #on parcourt la liste des distances et on recupere l'indice qui correspond a la distance minimale
        #Cet indice de distance minimal correspondra à l'indice de placement du centre dans le tableau centre décalé de 1 avec lequel on a calculé la distance
        indice_mini=distance[i].index(min(distance[i]))
        #on construit une nouvelle avec les valeurs de data et on rajoute ensuite l'indice du centre correspondant
        affect.append(data[i])
        affect[i].append(centre[indice_mini-1][0])
    return affect

###################################################################################################################################### 

 
def regrouper(affect, centres, i):
    '''
    regroupe les observations selon les centres correspondants

    @param centres: la liste des centres 

    @param affect: la liste des affectations

    @param i: nombre de fois d'utiliser cette fonction

    @return: un dictionnaire constitué des differents groupes

    '''
    groupe={}
    # pour chaque centre donnee on regarde si l'observation est affecte au centre 
    # et si c'est le cas on le stocke dans le dictionnaire avec comme clé le numero du centre
    for centre in centres: 
        donnees=[]
        donnees=([donnee for donnee in affect if donnee[-1]==centre[0]])
        for i in range(0,len(donnees)):
           # ici on enleve le numero du centre qui se trouve dans la liste affect
           del donnees[i][-1]   
        # pour ce centre donnee on a son groupe 
        groupe[centre[0]]=donnees

    #faire plot des points    
        
    return groupe




###################################################################################################################################### 

def nouveauCentre(groupe):
    ''' 
    cree un nouveau centre d'un groupe donne

    @param groupe: l'ensemble des clusters, c'est un dictionnaire constitué tous les différents groupes  

    @return: une liste formée des nouveaux centres 

    '''
    newCenter=[]    
    # on parcourt notre dictionnaire avec les clés
    for element in groupe.keys():
        centre=[]
        k=0
        #on cree un centre et on y rajoute les numeros des centres precendents comme clé dans la nouvelle liste de centres
        centre.append(element)
        for i in range(1,len(groupe[element][k])):
            som=0
            moy=0
            #on calcule le nouveau centre comme moyenne de toutes les observations dans le meme groupe 
            for j in range(0,len(groupe[element])):
                som+=groupe[element][j][i]
            k+=1
            moy= som/ len(groupe[element])
            # on stocke la nouvelle moyenne calculée dans centre
            centre.append(moy)
            # on stocke enfin ce centre dans la liste de centres newCenter
        newCenter.append(centre)
    return newCenter



######################################################################################################################################



def calculDiff ( listeA, listeB):
    '''
    calcule la difference des valeurs de deux listes donnees

    @param listeA: la premiere liste donnee

    @param listeB: la deuxieme liste donnee

    @return: retourn False si on a une difference de valeurs entre les deux listes sinon retourne true

    '''
    # retourne False si une des listes est vide
    if (listeA==[] or listeB==[]):
        return False
    # aucune liste n'est vide, dans ce cas on calcule la difference des valeurs entre les deux listes
    else:
        # on cree une liste de liste diff qui prendra comme valeurs des boolean
        diff=[0]*len(listeA)
        #on cree une nouvelle qui prendra des boolean issus d'une requete de la liste diff
        difference=[0]*len(listeA)
        # on parcourt les deux listes en calculant la difference pour chaque valeur
        for j in range(0,len(listeA)):
            # declaration de la liste diff comme liste de listes
            diff[j]=[0]*(len(listeA[j])-1)
            for i in range(1,len(listeA[j])):
                #on verifie si la difference est negligeable et dans ce cas la valeur de diff[j] prendra des boolean
                diff[j][i-1]=(abs(listeA[j][i]-listeB[j][i]))<=10**(-8)
            #on regarde si pour chaque liste dans diff si les valeurs sont toutes true et on le stocke ensuite dans difference
            difference[j]=all(diff[j])
        print diff
        print difference
        # on retourne true si toutes les valeurs de difference sont true
        # ca veut dire qu'il y a pas de defference entre les deux listes 
        return all(difference)


######################################################################################################################################

def read_iris(filename):
    '''
    récuperer les donées dans le fichier Iris.data. 

    @param filename: le nom du fichier où on récupère les données 

    '''
    
    f = open(filename)
    lines = f.readlines()
    data = []
    for i in xrange(len(lines)-1):   
        l = lines[i].split(',')
        line = [i+1]+[float(x) for x in l[:-1]]
        data.append(line)
    f.close()
    
    return data

######################################################################################################################################

def plot2D(groupe, centres, i):
    '''
    afficher les points en 2D

    @param groupe: dictionnaire dont les centres comme les clés avec ses points corresponds

    @param centres: liste de tous les centres

    @param i: nombre de fois d'utiliser la fonction regrouper 

    '''
    style = '.'
    fig2D = plt.figure(i)
    listeColor=['r','g','b','c','m','y']#il y aura 6 groupe maximum de couleurs déffirentes
    
    counterCentres=0 # donner un numéro de couleur dans la liste
    for centre in centres:
        color=listeColor[counterCentres]
        x=centre[1]
        y=centre[2]
        plt.scatter(x, y,  c=color, s=150, marker='+')
        for point in groupe[centre[0]]:
            x=point[1]
            y=point[2]
            plt.scatter(x, y,   c=color,s=80, marker='.')
        counterCentres=counterCentres+1
    plt.savefig("image{0}.pdf".format(i))

######################################################################################################################################

def plot3D(groupe, centres, i):
    '''
    afficher les points en 3D

    @param groupe: dictionnaire dont les centres comme les clés avec ses points corresponds

    @param centres: liste de tous les centres

    @param i: nombre de fois d'utiliser la fonction regrouper

    '''
    style = '.'
    fig3D = plt.figure(100*i)
    
    ax = fig3D.add_subplot(111, projection='3d')

    listeColor=['r','g','b','c','m','y']#il y aura 6 groupe maximum de couleurs déffirentes
    counterCentres=0 # donner un numéro de couleur dans la liste
    for centre in centres:
        color=listeColor[counterCentres]
        x=centre[1]
        y=centre[2]
        z=centre[3]
        ax.scatter(x,y,z,color,marker='+')
        for point in groupe[centre[0]]:
            x=point[1]
            y=point[2]
            z=point[3]
            ax.scatter(x,y,z, color, marker='.')
        counterCentres=counterCentres+1
    plt.savefig("image{0}.pdf".format(i))









          
     
######################################## algorithme k_means ##########################################################################
      
######################################################################################################################################
# commencer à programmer !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


#Pour les donnees d'entree
#on peut traiter soit les donnée aléatoires soit les donnée dans le fichier iris

#demander quel type de donnée pour traiter 
#1 pour le type de donnée aléatoire 2 pour les données dans le fichier iris

val=(raw_input('quel type de données que vous voulez traiter(1 données aléatoire, 2 donnée dans le fiichier iris ) '))
val1=(int ( val)) 
if val1==1:
    
    #génèrer des données aléatoires
    data = generate_random_data(100,2)
    print data

if val1==2:

    #génèrer des données dans le fichier iris
    data =  read_iris("iris.data")
    print data


# Ensuite on ecrit ces donnees dans un fichier
write_data(data, "donnee_entree.csv")

# Puis on relit ces donnees a partir du fichier
data_read = read_data("donnee_entree.csv")


#pour les centres
# on genere les centres des donnees

centre=generate_random_centres(3,data)
#print("centre")
#print centre

# Ecrire les centres dans un fichier
write_data(centre, "donnee_centre.csv")

# Relire les centres a partir du fichier
data_read = read_data("donnee_centre.csv")

# on calcule la distance entre les donnees et les centres
distance=calcul_distance(centre,data)
#print distance


#pour les affectations
# on affecte à chaque individu son groupe correspondant à l'indice du centre dont il est le plus pret 

affect=affectation(distance,centre,data)
#print affect

# Ecrire les affectations dans un fichier
write_data(affect, "affectation.csv")

# Relire les affectations a partir du fichier
data_read = read_data("affectation.csv")

#pour les nouveaux groupes après la première fois de recalculer des groupes
groupe={}
i=1
groupe= regrouper(affect,centre,i)
print groupe

# pour faire le plot si le legth de data est de 3 ou de 4
# ca veut dire que il y a 2 valeurs ou 3 valeurs avec le premier 
# chiffire est son numéro 
if len(centre[0])==3:
    plot2D(groupe, centre, i)
    print "image créée" + str(i)
    i=i+1    
if len(centre[0])==4:
    plot3D(groupe, centre, i)
    print "image créée" + str(i)
    i=i+1

# pour écrire les groupe dans un fichier afin de le comparer avec l'ancien
groupeData=[]
for key in groupe.keys():
    for value in groupe[key]:
        value=value+[key]
        groupeData.append(value)    
write_data(groupeData, "groupe.data")



    


#pour les nouveaux centres

ancienCentre=[]
ancienCentre= nouveauCentre(groupe)
#print ancienCentre

# comparer avec le nouveau centre
nouvCentre=[]
while calculDiff(nouvCentre,ancienCentre)==False :
    # on calcule la distance entre les donnees et les centres
    nouvCentre= ancienCentre
    distance=calcul_distance(ancienCentre,data)
    #print("la distance")
    #print distance
    affect=affectation(distance,ancienCentre,data)
    #print("affectation")
    #print affect
    groupe={}
    groupe= regrouper(affect,ancienCentre,i)

    #calculer nombre de points dans les groupes
    for element in groupe.keys():
        print (str(len(groupe[element]))+" points dans le groupe dont le centre est: " + str(element))

    if len(ancienCentre[0])==3:
        plot2D(groupe, ancienCentre, i)
        print "image créée" + str(i)
        i=i+1    
    if len(ancienCentre[0])==4:
        plot3D(groupe, ancienCentre, i)
        print "image créée" + str(i)
        i=i+1

    #print ("groupe")
    #print groupe
    ancienCentre= nouveauCentre(groupe)
    #print ("nouveau centre")
    #print ancienCentre

if len(ancienCentre[0])==3:
    plot2D(groupe, ancienCentre, i)
    print "image créée" + str(i)
    i=i+1    
if len(ancienCentre[0])==4:
    plot3D(groupe, ancienCentre, i)
    print "image créée" + str(i)
    i=i+1








