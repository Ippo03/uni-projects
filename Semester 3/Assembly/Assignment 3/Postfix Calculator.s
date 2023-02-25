#PANTELIDIS IPPOKRATIS p3210150
#MITSAKIS NIKOS p3210122
#PANAGIOTIS MOSCHOS p3210127	

.text
.globl main
#main
main:	
	#result
	li $t0,0 
	#values in stack
	li $t2,0							
  			
	#print mhnyma 1
    li $v0,4							
    la $a0, input_prompt
    syscall
	
read_token: 
	#read ch(char) 
    li $v0, 12							
    syscall
	
	#load ch in $t1
	sh $v0,ch							
	lb $t1,ch
	
	#if(ch == ' ') read next ch
	beq $t1,' ',read_token		
	#number = 0
	li $t5,0	

#while ((ch >='0') && (ch<='9'))	
digit:		
	#if ch < ascii code of 0 go to label checkop
	blt $t1,48,checkop	
	#if ch > ascii code of 9 go to label checkop
	bgt $t1,57,checkop	
	#int value of ch
	addi $t1, $t1, -48 	
	#10*number
	mul $t5,$t5,10	
	#number = 10*number + (ch-48)
	add $t5,$t5,$t1						
	
	#read next ch
	li $v0,12							
    syscall
	
	#load ch in $t1
	sh $v0,ch							
	lb $t1,ch
	
	#jump to the start of the while loop
	j digit								

#ch not ' ', not value,check if operator,true goto to operator	
checkop:								
	beq $t1,'+',operator
	beq $t1,'-',operator
	beq $t1,'*',operator
	beq $t1,'/',operator
	#if not +,-,*,/ goto label notequal
	j not_equal							
	
operator:
	#call pop function
	jal pop		
	#x2= pop()
    move $t6, $v0	
	#call pop function
    jal pop	
	#x1= pop()
    move $t7, $v0
	#calc arguments
	#x2
	move $a0, $t6 
	#ch
    move $a1, $t1
	#x1
    move $a2, $t7 
	#call calc function
    jal calc
	#return value of calc->argument for push
	move $a0,$v0
	#call push function
	jal push		
	#go to label read_token
	j read_token						

not_equal: 
	#if ch == '=' go to final
	beq $t1,'=',final
	#number->argument for push
	move $a0,$t5
	#call push function
	jal push
	#go to label read_token
	j read_token						
	
final:
	#if stack has one value print else goto label invalid
	bne $t2,1,invalid
	#print mynhma 5
	li $v0, 4							
    la $a0, output_msg
    syscall
	
	#load and print value at the top of the stack
    lw $a0,0($sp)						
    li $v0,1							
    syscall
	
	#goto exit
	j exit 								

#subprogramms
#pop function
pop:			
	#check if stack is empty
    beq $t2,0,invalid
	#decrease values in stack by 1
	sub $t2,$t2,1	
	#load value at the top of the stack
    lw $v0,0($sp)	
	#increase stack pointer by 4
	add $sp,$sp,4	
	#return where called
    jr $ra								

#calc function
calc:	
	#check value of ch and perform calculation
    beq $a1, '+', plus					
    beq $a1, '-', minus
    beq $a1, '*', times
    beq $a1, '/', divide
    j end

plus:	
	#x1 + x2
    add $v0, $a2, $a0					
    j end

minus:
	#x1 - x2
    sub $v0, $a2, $a0					
    j end

times:
	# x1 * x2
    mul $v0, $a2, $a0					
    j end

divide:
	#if x2 != 0 else goto label divide_by_zero
    beq $a0, 0, divide_by_zero	
	#x1 / x2
    div $v0, $a2, $a0					
    j end

divide_by_zero:
	#print mynhma 4
    li $v0, 4							
    la $a0, error_div
    syscall
	
	#goto exit
    j exit								

end:
	#return where called
    jr $ra								
	
#push function
push:		
	#check if stack is full
    beq $t2,20,full	
	#increase values in stack by 1
	add $t2,$t2,1	
	#decrease stack pointer by 4
    add $sp, $sp,-4			
	#store value at the top of the stack
	sw $a0,0($sp)
	#return where called					
    jr $ra								

invalid:
	#print mynhma 2
    li $v0, 4							
    la $a0, error_msg
    syscall 
	
	#goto exit
	j exit								
	
full:
	#print mynhma 3
    li $v0, 4							
    la $a0, full_stack
    syscall
	
	#goto exit
	j exit								
   
exit: 
	#exit program
	li $v0,10							
	syscall
	
.data
#character(1 byte)
ch:			  .space 1					
#mhnyma 1		
input_prompt: .asciiz "Postfix (input):"	
#mhnyma 2	
error_msg: .asciiz "\nInvalid Postfix"		
#mhnyma 3	
full_stack: .asciiz "\nThe stack is full"		
#mhnyma 4
error_div: .asciiz "\nDivide by zero"		
#mhnyma 5	
output_msg: .asciiz "\nPostfix Evaluation: "	
