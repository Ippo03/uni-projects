#IPPOKRATIS PANTELIDIS p3210150
	
.data
#prwtos ari8mos(4 bytes)
num1:       .space 4
#deyterosari8mos(4 bytes)			
num2:       .space 4  
#apotelesma(4 bytes)			
res:        .space 4
#telesths(1 byte)			
op:         .space 1	
#mynhmata		
test1:		.asciiz "test"	
#1	
prompt:     .asciiz "\nNumber:" 
test2:		.asciiz "test1"
#2
prompt1:    .asciiz "Operator:" 
#3	
prompt2:    .asciiz "\nError: Invalid operator." 
#4
prompt3:    .asciiz "Error: Divide by zero."
#5	
prompt4:    .asciiz "\nDo you want to continue with a new expression? (y/n)" 
#6
prompt5:	.asciiz "\nThe result is:" 
#telesths(1 byte)
ans:		.space 1			
			
.text
.globl main
	
main:	
		
loop1:	
	#Emfanise mynhma 1
	la $a0,prompt		
	li $v0,4
	syscall
		
	#read num1
	li $v0,5			
	syscall
		
	#apotelesma = num1
	sw $v0,res			
	lw $t0,res
	
	#Emfanise mynhma 2
	la $a0,prompt1		
	li $v0,4
	syscall
			
	#read operator
	li $v0,12			
	syscall
			
	sh $v0,op		
	lb $t1,op
		
	#anatheseis
	li $t3,'+'			
	li $t4,'-'
	li $t5,'*'
	li $t6,'/';
	li $t7,'%'
	li $t8,'='
		
s0:			
	#if op=='+' kane prosthesi else go to s1
	bne $t1,$t3,s1
	#Emfanise mynhma 1
	la $a0,prompt 		
	li $v0,4
	syscall
	
	#read num2
	li $v0,5			
	syscall
	sw $v0,num2
	lw $t2,num2
		
	add $t0,$t0,$t2
			
	#Emfanise mynhma 2
	la $a0,prompt1 		
	li $v0,4
	syscall
	
	#read op
	li $v0,12			
	syscall
			
	sh $v0,op			
	lb $t1,op
			
s1:	
	#if op=='-' kane afairesh else go to s2
	bne $t1,$t4,s2
	#Emfanise mynhma 1
	la $a0,prompt		
	li $v0,4
	syscall
		
	#read num2
	li $v0,5			
	syscall
	
	sw $v0,num2
	lw $t2,num2
			
	sub $t0,$t0,$t2
			
	#Emfanise mynhma 2
	la $a0,prompt1 		
	li $v0,4
	syscall
			
	#read op
	li $v0,12			
	syscall
			
	sh $v0,op			
	lb $t1,op
			
s2:   	
	#if op=='*' kane afairesh else go to s3
	bne $t1,$t5,s3	
	#Emfanise mynhma 1
	la $a0,prompt		
	li $v0,4
	syscall
			
	#read num2
	li $v0,5			
	syscall
	
	sw $v0,num2
	lw $t2,num2
			
	mul $t0,$t0,$t2
			
	#Emfanise mynhma 2
	la $a0,prompt1		
	li $v0,4
	syscall
			
	#read op
	li $v0,12			
	syscall
			
	sh $v0,op			
	lb $t1,op
			
s3:	
	#if op=='/' kane diairesh else go to s4
	bne $t1,$t6,s4	
	#Emfanise mynhma 1
	la $a0,prompt		
	li $v0,4
	syscall
		
	#read num2
	li $v0,5			
	syscall
	
	sw $v0,num2
	lw $t2,num2
			
	#if num!=0 go to divi else Emfanise mynhma 4
	bne $t2,$zero,divi	
	la $a0,prompt3
	li $v0,4
	syscall
			
	#go to exit
	j exit				
				
s4:	
	#if op=='%' kane modulo else go to s5
	bne $t1,$t7,s5	
	#Emfanise mynhma 1
	la $a0,prompt		
	li $v0,4
	syscall
		
	#read num2
	li $v0,5			
	syscall
	
	sw $v0,num2
	lw $t2,num2
		
	#if num!=0 go to modu else Emfanise mynhma 4
	bne $t2,$zero,modu	
	la $a0,prompt3
	li $v0,4
	syscall
			
	#go to exit
	j exit	
				
divi:
	div $t0,$t0,$t2
		
	#Emfanise mynhma 2
	la $a0,prompt1 		
	li $v0,4
	syscall
		
	#read op
	li $v0,12			
	syscall
			
	sh $v0,op			
	lb $t1,op
			
	j s0
modu:	
	rem $t0,$t0,$t2
	
	#Emfanise mynhma 2
	la $a0,prompt1 		
	li $v0,4
	syscall
	
	#read op
	li $v0,12			
	syscall
		
	sh $v0,op			
	lb $t1,op		
			
	j s0
			
s5:
	#if op=='=' Emfanise mynhma 3 else go to s6
	beq $t1,$t8,s6		
	la $a0,prompt2	
	li $v0,4
	syscall
	
	#go to exit
    	j exit				
	
s6:
	#Emafanise mynhma 6
	la $a0,prompt5		
	li $v0,4
	syscall
		
	#Ektypwse to apotelesma
	move $a0,$t0		
	li $v0,1
	syscall
		
	#Emafanise mynhma synexeias 5
	la $a0,prompt4		
	li $v0,4
	syscall
			
	#read op
	li $v0,12			
	syscall
			
	sh $v0,ans			
	lb $t9,ans
			
	#if ans=='y' go to loop1
	beq $t9,'y',loop1 
	
	#go to exit
	j exit	
	
exit:
	#exit
	li $v0,10
	syscall