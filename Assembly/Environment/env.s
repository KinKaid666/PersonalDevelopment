	.text
	.global main

main:
	enter	$0, $0
	movl	16(%esp), %ebx

loop:

	pushl	(%ebx)
	call	puts
	addl	$4, %esp

	addl	$4, %ebx
	cmp	$0, (%ebx)
	jne	loop

	leave
	ret
