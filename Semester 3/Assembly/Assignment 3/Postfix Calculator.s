	#IPPOKRATIS PANTELIDIS p3210150
	
	.data
num1:       .space 4			#prwtos ari8mos(4 bytes)
num2:       .space 4  			#deyterosari8mos(4 bytes)
res:        .space 4			#apotelesma(4 bytes)
op:         .space 1			#telesths(1 byte)
test1:		.asciiz "test"		#mynhmata
prompt:     .asciiz "\nNumber:" #1
test2:		.asciiz "test1"
prompt1:    .asciiz "Operator:" #2	
prompt2:    .asciiz "\nError: Invalid operator." #3
prompt3:    .asciiz "Error: Divide by zero."	#4
prompt4:    .asciiz "\nDo you want to continue with a new expression? (y/n)" #5
prompt5:	.asciiz "\nThe result is:" #6
ans:		.space 1			#telesths(1 byte)
			
		    .text
			.globl main
	
	main:	
		
	loop1:	la $a0,prompt		#Emfanise mynhma 1
			li $v0,4
			syscall
			
			li $v0,5			#read num1
			syscall
			
			sw $v0,res			#apotelesma = num1
			lw $t0,res
			
			la $a0,prompt1		#Emfanise mynhma 2
			li $v0,4
			syscall
			
			li $v0,12			#read operator
			syscall
			
			sh $v0,op		
			lb $t1,op
			
			li $t3,'+'			#anatheseis
			li $t4,'-'
			li $t5,'*'
			li $t6,'/';
			li $t7,'%'
			li $t8,'='
			
	s0:		bne $t1,$t3,s1		#if op=='+' kane prosthesi else go to s1
			la $a0,prompt 		#Emfanise mynhma 1
			li $v0,4
			syscall
				
			li $v0,5			#read num2
			syscall
			sw $v0,num2
			lw $t2,num2
			
			add $t0,$t0,$t2
			
			la $a0,prompt1 		#Emfanise mynhma 2
			li $v0,4
			syscall
			
			li $v0,12			#read op
			syscall
			
			sh $v0,op			
			lb $t1,op
			
	s1:		bne $t1,$t4,s2		#if op=='-' kane afairesh else go to s2
			la $a0,prompt		#Emfanise mynhma 1
			li $v0,4
			syscall
				
			li $v0,5			#read num2
			syscall
			sw $v0,num2
			lw $t2,num2
			
			sub $t0,$t0,$t2
			
			la $a0,prompt1 		#Emfanise mynhma 2
			li $v0,4
			syscall
			
			li $v0,12			#read op
			syscall
			
			sh $v0,op			
			lb $t1,op
			
	s2:   	bne $t1,$t5,s3		#if op=='*' kane afairesh else go to s3
			la $a0,prompt		#Emfanise mynhma 1
			li $v0,4
			syscall
				
			li $v0,5			#read num2
			syscall
			sw $v0,num2
			lw $t2,num2
			
			mul $t0,$t0,$t2
			
			la $a0,prompt1		#Emfanise mynhma 2
			li $v0,4
			syscall
			
			li $v0,12			#read op
			syscall
			
			sh $v0,op			
			lb $t1,op
			
	s3:		bne $t1,$t6,s4		#if op=='/' kane diairesh else go to s4
			la $a0,prompt		#Emfanise mynhma 1
			li $v0,4
			syscall
			
			li $v0,5			#read num2
			syscall
			sw $v0,num2
			lw $t2,num2
			
			bne $t2,$zero,divi	#if num!=0 go to divi else Emfanise mynhma 4
			la $a0,prompt3
			li $v0,4
			syscall
			
			j exit				#go to exit
			
			
	s4:		bne $t1,$t7,s5		#if op=='%' kane modulo else go to s5
			la $a0,prompt		#Emfanise mynhma 1
			li $v0,4
			syscall
			
			li $v0,5			#read num2
			syscall
			sw $v0,num2
			lw $t2,num2
			
			bne $t2,$zero,modu	#if num!=0 go to modu else Emfanise mynhma 4
			la $a0,prompt3
			li $v0,4
			syscall
			
			j exit	#go to exit
			
			
	divi:
			div $t0,$t0,$t2
			
			la $a0,prompt1 		#Emfanise mynhma 2
			li $v0,4
			syscall
			
			li $v0,12			#read op
			syscall
			
			sh $v0,op			
			lb $t1,op
			
			j s0
	modu:	
			rem $t0,$t0,$t2
			la $a0,prompt1 		#Emfanise mynhma 2
			li $v0,4
			syscall
			
			li $v0,12			#read op
			syscall
			
			sh $v0,op			
			lb $t1,op		
			
			j s0
			
	s5:		beq $t1,$t8,s6		#if op=='=' emfanise mynhma 3 else go to s6
			la $a0,prompt2	
			li $v0,4
			syscall
		    j exit				#go to exit
	
	s6:		la $a0,prompt5		#emafanise mynhma 6
			li $v0,4
		    syscall
										
			move $a0,$t0		#Ektypwse to apotelesma
			li $v0,1
		    syscall
			
			la $a0,prompt4		#Emafanise mynhma synexeias 5
			li $v0,4
		    syscall
			
			li $v0,12			#read op
			syscall
			
			sh $v0,ans			
			lb $t9,ans
			
			beq $t9,'y',loop1 	#if ans=='y' go to loop1
			j exit				#go to exit
			
	exit:	li $v0,10
			syscall