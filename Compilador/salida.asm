.386
.MODEL flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
_c@main DW ? 
cadena DB "cadena", 0
_b@main DW ? 
_a@main DW ? 
_j@main DW ? 
@aux1 DW ? 

DivisionCero db "Error al intentar dividir por 0", 0
RestaNegativa db "Ocurrió overflow(resta negativa) en la resta", 0
estado DW ?
.code
start: 
LabelDivisionCero:
invoke MessageBox, NULL, addr DivisionCero, addr DivisionCero, MB_OK
invoke ExitProcess, 0
LabelRestaNegativa:
invoke MessageBox, NULL, addr RestaNegativa, addr RestaNegativa, MB_OK
invoke ExitProcess, 0
MOV CX,_a@main
SUB CX,_b@main
JS LabelRestaNegativa
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
JS LabelRestaNegativa
MOV _a@main,BX
L23:
invoke MessageBox, NULL, addr OUT, addr cadena,MB_OK
end start 