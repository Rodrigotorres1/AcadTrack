-- Normaliza os nomes das turmas para o padrão "Xº Y" (com espaço entre º e a letra)

UPDATE turma SET nome = '1º A' WHERE nome = '1ºA';
UPDATE turma SET nome = '1º B' WHERE nome = '1ºB';
UPDATE turma SET nome = '2º A' WHERE nome = '2ºA';
UPDATE turma SET nome = '2º B' WHERE nome = '2ºB';
UPDATE turma SET nome = '3º A' WHERE nome = '3ºA';
UPDATE turma SET nome = '3º B' WHERE nome = '3ºB';
