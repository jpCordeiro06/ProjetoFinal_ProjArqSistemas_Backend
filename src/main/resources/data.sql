-- ============================================================
-- 1. TABELAS AUXILIARES (Status, Categoria, Condição)
-- ============================================================

-- Categorias
INSERT INTO categorias (id, nome) VALUES (1, 'Jogos');
INSERT INTO categorias (id, nome) VALUES (2, 'Consoles');
INSERT INTO categorias (id, nome) VALUES (3, 'Colecionáveis');

-- Condições
INSERT INTO condicoes (id, nome) VALUES (1, 'Novo');
INSERT INTO condicoes (id, nome) VALUES (2, 'Usado');

-- Status de Venda (ESSENCIAL PARA A NOVA LÓGICA)
INSERT INTO status_venda (id, descricao) VALUES (1, 'AGUARDANDO_PAGAMENTO');
INSERT INTO status_venda (id, descricao) VALUES (2, 'FINALIZADA');
INSERT INTO status_venda (id, descricao) VALUES (3, 'CANCELADA');

-- ============================================================
-- 2. USUÁRIOS E PERFIS
-- ============================================================

-- Perfis (Herança)
INSERT INTO perfis_usuario (id, descricao) VALUES (1, 'Administrador');
INSERT INTO perfis_administrador (id) VALUES (1);

INSERT INTO perfis_usuario (id, descricao) VALUES (2, 'Vendedor');
INSERT INTO perfis_vendedor (id) VALUES (2);

-- Usuários
INSERT INTO usuarios (id, nome, login, senha, perfil_id)
VALUES (1, 'Gerente Master', 'admin', '123456', 1);

INSERT INTO usuarios (id, nome, login, senha, perfil_id)
VALUES (2, 'João da Silva', 'joao', '123456', 2);

-- Clientes
INSERT INTO clientes (id, nome, cpf, email)
VALUES (1, 'Maria Compradora', '111.222.333-44', 'maria@gmail.com');

-- ============================================================
-- 3. PRODUTOS E ESTOQUE (AQUI MUDOU TUDO!)
-- ============================================================

-- Produto 1: Jogo God of War (Sem quantidade aqui!)
INSERT INTO produtos (id, nome, preco, categoria_id, condicao_id)
VALUES (1, 'God of War Ragnarok', 299.90, 1, 1);
INSERT INTO jogos (produto_id, plataforma) VALUES (1, 'PS5');

-- Estoque do Produto 1 (Tabela Separada)
INSERT INTO estoques (id, quantidade) VALUES (1, 10);


-- Produto 2: Console PS5
INSERT INTO produtos (id, nome, preco, categoria_id, condicao_id)
VALUES (2, 'PlayStation 5 Slim', 3500.00, 2, 1);
INSERT INTO consoles (produto_id, marca, modelo) VALUES (2, 'Sony', 'CFI-2000');

-- Estoque do Produto 2
INSERT INTO estoques (id, quantidade) VALUES (2, 5);


-- Produto 3: Funko Pop (Colecionável)
INSERT INTO produtos (id, nome, preco, categoria_id, condicao_id)
VALUES (3, 'Funko Pop Homem de Ferro', 150.00, 3, 1);
INSERT INTO colecionaveis (produto_id, linha, fabricante) VALUES (3, 'Marvel', 'Funko');

-- Estoque do Produto 3
INSERT INTO estoques (id, quantidade) VALUES (3, 20);

-- ============================================================
-- 4. CUPONS DE DESCONTO (NOVO!)
-- ============================================================

INSERT INTO cupons_desconto (codigo, percentual, validade, ativo)
VALUES ('BEMVINDO10', 10.00, '2030-12-31', true);

INSERT INTO cupons_desconto (codigo, percentual, validade, ativo)
VALUES ('PROMO50', 50.00, '2030-12-31', true);

ALTER TABLE produtos ALTER COLUMN id RESTART WITH 4;
ALTER TABLE estoques ALTER COLUMN id RESTART WITH 4;
