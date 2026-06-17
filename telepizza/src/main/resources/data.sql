-- Limpeza para permitir reexecução sem violar PK/FK (ordem: dependentes -> bases)
DELETE FROM pedidos; -- depende de clientes
DELETE FROM cardapio_produto;
DELETE FROM produto_receita;
DELETE FROM receita_ingrediente;
DELETE FROM produtos;
DELETE FROM receitas;
DELETE FROM cardapios;
DELETE FROM ingredientes;
DELETE FROM clientes;

-- Inserção dos clientes
INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES ('9001', 'Huguinho Pato', '51985744566', 'Rua das Flores, 100', 'huguinho.pato@email.com');
INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES ('9002', 'Luizinho Pato', '5199172079', 'Av. Central, 200', 'zezinho.pato@email.com');

-- Inserção dos ingredientes
INSERT INTO ingredientes (id, descricao) VALUES (1, 'Disco de pizza');
INSERT INTO ingredientes (id, descricao) VALUES (2, 'Porcao de tomate');
INSERT INTO ingredientes (id, descricao) VALUES (3, 'Porcao de mussarela');
INSERT INTO ingredientes (id, descricao) VALUES (4, 'Porcao de presunto');
INSERT INTO ingredientes (id, descricao) VALUES (5, 'Porcao de calabresa');
INSERT INTO ingredientes (id, descricao) VALUES (6, 'Molho de tomate (200ml)');
INSERT INTO ingredientes (id, descricao) VALUES (7, 'Porcao de azeitona');
INSERT INTO ingredientes (id, descricao) VALUES (8, 'Porcao de oregano');
INSERT INTO ingredientes (id, descricao) VALUES (9, 'Porcao de cebola');

-- Inserção das receitas 
INSERT INTO receitas (id, titulo) VALUES (1, 'Pizza calabresa');
INSERT INTO receitas (id, titulo) VALUES (2, 'Pizza queijo e presunto');
INSERT INTO receitas (id, titulo) VALUES (3, 'Pizza margherita');

-- Associação dos ingredientes à receita Pizza calabresa
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (1, 5); -- Porcao de calabresa
-- Associação dos ingredientes à receita Pizza queijo e presunto
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (2, 4); -- Porcao de presunto
-- Associação dos ingredientes à receita Pizza margherita
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 1); -- Disco de pizza
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 6); -- Molho de tomate (200ml)
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 3); -- Porcao de mussarela
INSERT INTO receita_ingrediente (receita_id, ingrediente_id) VALUES (3, 8); -- Porcao de cebola

-- insercao dos produtos
INSERT INTO produtos (id,descricao,preco) VALUES (1,'Pizza calabresa',5500);
INSERT INTO produtos (id,descricao,preco) VALUES (2,'Pizza queijo e presunto',6000);
INSERT INTO produtos (id,descricao,preco) VALUES (3,'Pizza margherita',4000);

-- Associação dos produtos com as receitas
INSERT INTO produto_receita (produto_id,receita_id) VALUES(1,1);
INSERT INTO produto_receita (produto_id,receita_id) VALUES(2,2);
INSERT INTO produto_receita (produto_id,receita_id) VALUES(3,3);

-- Insercao dos cardapios
INSERT INTO cardapios (id,titulo) VALUES(1,'Cardapio de Agosto');
INSERT INTO cardapios (id,titulo) VALUES(2,'Cardapio de Setembro');

-- Associação dos cardapios com os produtos
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,1);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,2);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (1,3);

INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (2,1);
INSERT INTO cardapio_produto (cardapio_id,produto_id) VALUES (2,3);

-- Inserção de usuários simples
INSERT INTO usuarios (id, username, password, tipo) VALUES (1, 'admin', '123456', 'ADMIN');
INSERT INTO usuarios (id, username, password, tipo) VALUES (2, 'usuario', '123456', 'USUARIO');
-- Usuários vinculados aos clientes (username = email do cliente)
INSERT INTO usuarios (id, username, password, tipo) VALUES (3, 'huguinho.pato@email.com', '123456', 'USUARIO');
INSERT INTO usuarios (id, username, password, tipo) VALUES (4, 'zezinho.pato@email.com', '123456', 'USUARIO');

-- Pedido de exemplo para testes de status
INSERT INTO pedidos (id,cliente_cpf,status) VALUES (1,'9001','NOVO');
INSERT INTO pedidos (id,cliente_cpf,status) VALUES (2,'9002','APROVADO');
INSERT INTO pedidos (id,cliente_cpf,status) VALUES (3,'9001','PAGO');
