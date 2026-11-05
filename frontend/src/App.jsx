import { useCallback, useEffect, useMemo, useState } from 'react'
import {
  Activity, ArrowLeft, ArrowRight, BookOpen, CheckCircle2, ChevronDown, CircleHelp,
  CirclePlus, ClipboardList, Command, Database, Filter, FolderKanban, GitBranch,
  Layers3, LayoutDashboard, LoaderCircle, Plus, RefreshCw, Search, Sparkles,
  UserRound, Users, X, AlertCircle
} from 'lucide-react'
import { api } from './api'

const prioridadeNome = { ALTA: 'Alta', MEDIA: 'Média', BAIXA: 'Baixa' }
const prioridadeClasse = { ALTA: 'priority-high', MEDIA: 'priority-mid', BAIXA: 'priority-low' }

function formatarData(valor) {
  if (!valor) return '—'
  return new Date(valor).toLocaleDateString('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' })
}

function Modal({ titulo, descricao, onClose, children }) {
  useEffect(() => {
    const handler = (event) => { if (event.key === 'Escape') onClose() }
    document.addEventListener('keydown', handler)
    return () => document.removeEventListener('keydown', handler)
  }, [onClose])
  return (
    <div className="modal-backdrop" onMouseDown={(event) => event.target === event.currentTarget && onClose()}>
      <section className="modal" role="dialog" aria-modal="true" aria-label={titulo}>
        <div className="modal-head">
          <div><h2>{titulo}</h2><p>{descricao}</p></div>
          <button className="icon-btn" onClick={onClose} aria-label="Fechar"><X size={18} /></button>
        </div>
        {children}
      </section>
    </div>
  )
}

function NovoProjeto({ onClose, onCreated }) {
  const [form, setForm] = useState({ nome: '', descricao: '', contextoDominio: '' })
  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState('')
  async function enviar(event) {
    event.preventDefault(); setSalvando(true); setErro('')
    try {
      const novo = await api('/projetos', { method: 'POST', body: form })
      await onCreated(novo)
      onClose()
    } catch (e) { setErro(e.message) } finally { setSalvando(false) }
  }
  return <Modal titulo="Criar projeto" descricao="Organize histórias em um contexto de negócio." onClose={onClose}>
    <form className="form" onSubmit={enviar}>
      <label>Nome do projeto <input required maxLength={255} autoFocus value={form.nome} placeholder="Ex.: Biblioteca digital"
        onChange={e => setForm(f => ({ ...f, nome: e.target.value }))} /></label>
      <label>Descrição <textarea rows={3} value={form.descricao} placeholder="Qual problema este projeto resolve?"
        onChange={e => setForm(f => ({ ...f, descricao: e.target.value }))} /></label>
      <label>Contexto de domínio <textarea rows={3} value={form.contextoDominio} placeholder="Glossário, termos e regras do negócio"
        onChange={e => setForm(f => ({ ...f, contextoDominio: e.target.value }))} /></label>
      {erro && <p role="alert" className="form-error"><AlertCircle size={15} /> {erro}</p>}
      <div className="form-actions"><button className="btn ghost" type="button" onClick={onClose}>Cancelar</button>
        <button className="btn primary" disabled={salvando} type="submit">{salvando ? 'Salvando...' : 'Criar projeto'}</button></div>
    </form>
  </Modal>
}

function NovaHistoria({ projeto, usuarioId, onClose, onCreated }) {
  const [form, setForm] = useState({ titulo: '', papel: '', objetivo: '', beneficio: '', prioridade: 'MEDIA' })
  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState('')
  async function enviar(event) {
    event.preventDefault(); setSalvando(true); setErro('')
    try {
      const nova = await api(`/projetos/${projeto.id}/historias`, { method: 'POST', usuarioId, body: form })
      await onCreated(nova)
      onClose()
    } catch (e) { setErro(e.message) } finally { setSalvando(false) }
  }
  return <Modal titulo="Nova história de usuário" descricao={`Projeto: ${projeto.nome}`} onClose={onClose}>
    <form className="form" onSubmit={enviar}>
      <label>Título <input autoFocus required maxLength={255} value={form.titulo} placeholder="Ex.: Reservar livro disponível"
        onChange={e => setForm(f => ({ ...f, titulo: e.target.value }))} /></label>
      <div className="two-cols">
        <label>Como (papel) <input required value={form.papel} placeholder="leitor autenticado"
          onChange={e => setForm(f => ({ ...f, papel: e.target.value }))} /></label>
        <label>Prioridade <select value={form.prioridade} onChange={e => setForm(f => ({ ...f, prioridade: e.target.value }))}>
          <option value="ALTA">Alta</option><option value="MEDIA">Média</option><option value="BAIXA">Baixa</option>
        </select></label>
      </div>
      <label>Quero (objetivo) <textarea required rows={2} value={form.objetivo} placeholder="reservar um livro no catálogo"
        onChange={e => setForm(f => ({ ...f, objetivo: e.target.value }))} /></label>
      <label>Para (benefício) <textarea required rows={2} value={form.beneficio} placeholder="retirá-lo mais tarde"
        onChange={e => setForm(f => ({ ...f, beneficio: e.target.value }))} /></label>
      <div className="narrativa-hint"><Sparkles size={16} /> Como <strong>{form.papel || '...'}</strong>, quero <strong>{form.objetivo || '...'}</strong>, para <strong>{form.beneficio || '...'}</strong>.</div>
      {erro && <p role="alert" className="form-error"><AlertCircle size={15} /> {erro}</p>}
      <div className="form-actions"><button className="btn ghost" type="button" onClick={onClose}>Cancelar</button>
        <button className="btn primary" type="submit" disabled={salvando || !usuarioId}>{salvando ? 'Salvando...' : 'Criar história'}</button></div>
    </form>
  </Modal>
}

function Estado({ icon: Icon, titulo, descricao, action }) {
  return <div className="empty"><span className="empty-icon"><Icon size={25} strokeWidth={1.6}/></span>
    <h3>{titulo}</h3><p>{descricao}</p>{action}</div>
}

function App() {
  const [usuarios, setUsuarios] = useState([])
  const [usuarioId, setUsuarioId] = useState('')
  const [projetos, setProjetos] = useState([])
  const [projetoId, setProjetoId] = useState(null)
  const [historias, setHistorias] = useState([])
  const [prioridade, setPrioridade] = useState('TODAS')
  const [busca, setBusca] = useState('')
  const [historiaId, setHistoriaId] = useState(null)
  const [modal, setModal] = useState(null)
  const [carregando, setCarregando] = useState(true)
  const [carregandoHistorias, setCarregandoHistorias] = useState(false)
  const [erro, setErro] = useState('')
  const [erroHistorias, setErroHistorias] = useState('')
  const [revisao, setRevisao] = useState(0)

  const carregarBase = useCallback(async () => {
    setCarregando(true); setErro('')
    try {
      const [listaUsuarios, listaProjetos] = await Promise.all([api('/usuarios'), api('/projetos')])
      setUsuarios(listaUsuarios)
      setProjetos(listaProjetos)
      setUsuarioId(id => listaUsuarios.some(u => String(u.id) === String(id)) ? id : String(listaUsuarios[0]?.id ?? ''))
    } catch (e) { setErro(e.message) } finally { setCarregando(false) }
  }, [])
  useEffect(() => { carregarBase() }, [carregarBase])

  useEffect(() => {
    if (projetoId == null) { setHistorias([]); return }
    const controller = new AbortController()
    setCarregandoHistorias(true); setErroHistorias('')
    const filtro = prioridade === 'TODAS' ? '' : `?prioridade=${prioridade}`
    api(`/projetos/${projetoId}/historias${filtro}`, { signal: controller.signal })
      .then(setHistorias).catch(e => { if (e.name !== 'AbortError') setErroHistorias(e.message) })
      .finally(() => { if (!controller.signal.aborted) setCarregandoHistorias(false) })
    return () => controller.abort()
  }, [projetoId, prioridade, revisao])

  const projeto = projetos.find(p => p.id === projetoId)
  const usuario = usuarios.find(u => String(u.id) === String(usuarioId))
  const filtradas = useMemo(() => historias.filter(h => `${h.titulo} ${h.papel} ${h.objetivo}`.toLowerCase().includes(busca.toLowerCase().trim())), [historias, busca])
  const selecionada = historias.find(h => h.id === historiaId)
  const pendencias = historias.reduce((s,h) => s + h.sugestoesPendentes, 0)
  const cenarios = historias.reduce((s,h) => s + h.quantidadeCenarios, 0)

  async function projetoCriado(novo) {
    await carregarBase(); setProjetoId(novo.id); setPrioridade('TODAS');setHistoriaId(null)
  }
  async function historiaCriada(nova) {
    setPrioridade('TODAS');setHistoriaId(nova.id);setRevisao(v => v + 1)
  }
  function entrarProjeto(id) {setProjetoId(id);setPrioridade('TODAS');setBusca('');setHistoriaId(null)}
  function voltarProjetos() {setProjetoId(null);setHistoriaId(null);setBusca('')}

  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-symbol"><GitBranch size={21}/></div>
        <div><strong>Estúdio BDD</strong><span>Workspace acadêmico</span></div></div>
      <div className="sidebar-section-label">WORKSPACE</div>
      <nav className="sidebar-nav" aria-label="Navegação principal">
        <button className={`nav-item ${projetoId === null ? 'active' : ''}`} onClick={voltarProjetos}><LayoutDashboard size={18}/> Visão geral</button>
        <button className={`nav-item ${projetoId !== null ? 'active' : ''}`} onClick={() => projetoId ? setHistoriaId(null) : projetos[0] && entrarProjeto(projetos[0].id)}>
          <FolderKanban size={18}/> Histórias <span className="nav-count">{projetos.length}</span></button>
      </nav>
      <div className="sidebar-section-label second">SOBRE O PROJETO</div>
      <div className="sidebar-note"><div className="note-icon"><BookOpen size={16}/></div>
        <strong>Programação de Sistemas II</strong><p>Projeto 2026/2 • Semanas 1–3</p>
        <div className="note-progress"><span/></div><small>API completa · SPA inicial</small></div>
      <div className="sidebar-bottom"><span className="online-dot"/> Ambiente de desenvolvimento <span className="version">v0.3</span></div>
    </aside>

    <div className="main-area">
      <header className="topbar">
        <div className="breadcrumb"><span>Workspace</span><ArrowRight size={13} /> <strong>{projeto ? projeto.nome : 'Visão geral'}</strong></div>
        <div className="top-actions"><div className="avatar"><UserRound size={16}/></div><div className="user-switch"><label htmlFor="usuario-atual">Usuário de demonstração</label>
          <div className="select-wrap"><select id="usuario-atual" value={usuarioId} onChange={e => setUsuarioId(e.target.value)}>
            {usuarios.length === 0 && <option value="">Nenhum usuário cadastrado</option>}
            {usuarios.map(u => <option key={u.id} value={u.id}>{u.nome}</option>)}
          </select><ChevronDown size={13}/></div></div></div>
      </header>

      <main className="content">
        {erro && <div className="alert" role="alert"><AlertCircle size={17}/> Não foi possível conectar à API: {erro}<button onClick={carregarBase}><RefreshCw size={15}/> Tentar novamente</button></div>}
        {projetoId === null ? <>
          <div className="page-header"><div><div className="eyebrow"><span className="eyebrow-line"/> SEUS PROJETOS</div>
            <h1>Organize requisitos com clareza.</h1><p>Transforme necessidades em histórias de usuário e cenários verificáveis.</p></div>
            <button className="btn primary" onClick={() => setModal('projeto')}><Plus size={16}/> Novo projeto</button></div>
          <div className="insight-banner"><div className="banner-icon"><Sparkles size={24}/></div>
            <div><strong>Da ideia ao comportamento esperado.</strong><p>Documente regras em Gherkin, registre sugestões de IA e mantenha o controle das decisões.</p></div>
            <div className="banner-watermark"><GitBranch size={94} strokeWidth={1}/></div></div>
          <div className="section-heading"><div><h2>Projetos</h2><p>Selecione um projeto para explorar as histórias.</p></div><span className="small-count">{projetos.length} projeto(s)</span></div>
          {carregando ? <div className="loading"><LoaderCircle className="spin"/> Carregando projetos...</div> : projetos.length === 0 ?
            <Estado icon={FolderKanban} titulo="Nenhum projeto por aqui" descricao="Crie seu primeiro projeto ou execute a API com o perfil demo para carregar exemplos."
              action={<button className="btn primary" onClick={() => setModal('projeto')}><Plus size={15}/> Criar projeto</button>}/> :
            <div className="project-grid">{projetos.map((p,index) => <button className="project-card" key={p.id} onClick={() => entrarProjeto(p.id)}>
              <div className="card-top"><div className={`project-icon color-${index % 3}`}><Layers3 size={20}/></div><ArrowRight className="card-arrow" size={18}/></div>
              <strong>{p.nome}</strong><p>{p.descricao || 'Sem descrição cadastrada.'}</p>
              <div className="card-foot"><span><Database size={14}/> Criado em {formatarData(p.criadoEm)}</span><span className="card-link">Abrir projeto <ArrowRight size={14}/></span></div>
            </button>)}</div>}
          <div className="footer-tip"><CircleHelp size={16}/> A identidade do usuário é simulada com o cabeçalho X-Usuario-Id. Não substitui autenticação.</div>
        </> : <>
          <button className="back-link" onClick={voltarProjetos}><ArrowLeft size={15}/> Todos os projetos</button>
          <div className="page-header project-header"><div><div className="eyebrow"><span className="eyebrow-line"/> GERENCIAMENTO DE HISTÓRIAS</div>
            <h1>{projeto?.nome || 'Projeto'}</h1><p>{projeto?.descricao || 'Histórias e critérios de aceite organizados em um só lugar.'}</p></div>
            <button className="btn primary" disabled={!usuarioId} onClick={() => setModal('historia')}><Plus size={16}/> Nova história</button></div>
          <div className="stats-grid">
            <div className="stat"><div className="stat-icon blue"><ClipboardList size={19}/></div><span>Histórias {prioridade === 'TODAS' ? 'do projeto' : 'filtradas'}</span><strong>{historias.length}</strong><small>Requisitos documentados</small></div>
            <div className="stat"><div className="stat-icon violet"><GitBranch size={19}/></div><span>Cenários</span><strong>{cenarios}</strong><small>Comportamentos definidos</small></div>
            <div className="stat"><div className="stat-icon orange"><Activity size={19}/></div><span>Sugestões pendentes</span><strong>{pendencias}</strong><small>Aguardando decisão</small></div>
          </div>
          <div className="section-heading stories-heading"><div><h2>Histórias de usuário</h2><p>Consulte, filtre e acompanhe a documentação de cada história.</p></div>
            <button className="btn outline" onClick={() => setRevisao(v => v + 1)}><RefreshCw size={15}/> Atualizar</button></div>
          <div className="stories-layout"><section className="stories-panel">
            <div className="toolbar"><div className="search-input"><Search size={16}/><input aria-label="Pesquisar histórias" value={busca}
              placeholder="Buscar histórias..." onChange={e => setBusca(e.target.value)}/></div>
              <div className="filter-select"><Filter size={15}/><select aria-label="Filtrar prioridade" value={prioridade} onChange={e => {setPrioridade(e.target.value);setHistoriaId(null)}}>
                <option value="TODAS">Todas prioridades</option><option value="ALTA">Alta</option><option value="MEDIA">Média</option><option value="BAIXA">Baixa</option>
              </select><ChevronDown size={14}/></div></div>
            {erroHistorias ? <div className="inline-error" role="alert"><AlertCircle size={16}/>{erroHistorias}</div> : carregandoHistorias ?
              <div className="loading"><LoaderCircle className="spin"/> Carregando histórias...</div> : filtradas.length === 0 ?
              <Estado icon={ClipboardList} titulo="Nenhuma história encontrada" descricao="Crie uma história ou altere os filtros para ver resultados."
                action={<button className="btn outline" onClick={() => {setBusca('');setPrioridade('TODAS')}}>Limpar filtros</button>}/> :
              <div className="story-list">{filtradas.map(h => <button key={h.id} className={`story-row ${historiaId === h.id ? 'chosen' : ''}`} onClick={() => setHistoriaId(h.id)}>
                <div className="story-indicator"><BookOpen size={17}/></div>
                <div className="story-details"><strong>{h.titulo}</strong><p>Como {h.papel}, quero {h.objetivo}</p>
                  <div className="story-meta"><span><GitBranch size={13}/> {h.quantidadeCenarios} cenário(s)</span>
                    <span><Sparkles size={13}/> {h.sugestoesPendentes} pendente(s)</span></div></div>
                <span className={`priority ${prioridadeClasse[h.prioridade]}`}>{prioridadeNome[h.prioridade]}</span>
              </button>)}</div>}
          </section>
          <aside className="detail-panel">{selecionada ? <>
            <div className="detail-overline">HISTÓRIA SELECIONADA <span>#{selecionada.id}</span></div>
            <h3>{selecionada.titulo}</h3>
            <span className={`priority ${prioridadeClasse[selecionada.prioridade]}`}>{prioridadeNome[selecionada.prioridade]}</span>
            <div className="narrative"><span>COMO</span><p>{selecionada.papel}</p><span>QUERO</span><p>{selecionada.objetivo}</p><span>PARA</span><p>{selecionada.beneficio}</p></div>
            <div className="detail-sep"/>
            <div className="detail-facts"><span>Autor <strong>{selecionada.nomeAutor}</strong></span>
              <span>Criada em <strong>{formatarData(selecionada.criadoEm)}</strong></span>
              <span>Cenários <strong>{selecionada.quantidadeCenarios}</strong></span>
              <span>Pendências <strong>{selecionada.sugestoesPendentes}</strong></span></div>
            <div className="detail-foot"><CheckCircle2 size={16}/> Visualização inicial da Semana 3</div>
          </> : <Estado icon={BookOpen} titulo="Selecione uma história" descricao="Clique em uma história para visualizar sua narrativa e os indicadores."/>}</aside>
          </div>
          <div className="footer-tip"><CircleHelp size={16}/> A edição de cenários, a curadoria de sugestões e a exportação pela interface ficam para a Semana 4. Os endpoints já estão disponíveis na API.</div>
        </>}
      </main>
    </div>
    {modal === 'projeto' && <NovoProjeto onClose={() => setModal(null)} onCreated={projetoCriado}/>}
    {modal === 'historia' && projeto && <NovaHistoria projeto={projeto} usuarioId={usuarioId} onClose={() => setModal(null)} onCreated={historiaCriada}/>}
  </div>
}
export default App
