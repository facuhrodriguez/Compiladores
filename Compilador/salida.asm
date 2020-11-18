.386
.model flat
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
_c@main DW ? 
_b@main DW ? 
_a@main DW ? 
@aux1 DW ? 

.code
start: 
MOV CX,_a@main
SUB CX,_b@main
JS RestaNegativa
MOV BX,_c@main
ADD BX,_1
CMP CX,BX
JNA L17
MOV BX,_b@main
ADD BX,_c@main
MOV _a@main,BX
JMP L23
L17:
MOV BX,_b@main
SUB BX,_c@main
JS RestaNegativa
MOV _a@main,BX
L23:
DivisionCero db "Error al intentar dividir por 0", 0
RestaNegativa db "Ocurrió overflow(resta negativa) en la resta", 0
invoke ExitProcess, 0
end start