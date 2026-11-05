const BASE = (import.meta.env.VITE_API_URL || '/api').replace(/\/$/, '')

export async function api(path, { method = 'GET', usuarioId, body, signal } = {}) {
  const headers = { 'Accept': 'application/json' }
  if (body !== undefined) headers['Content-Type'] = 'application/json'
  if (usuarioId) headers['X-Usuario-Id'] = String(usuarioId)
  const response = await fetch(`${BASE}${path}`, {
    method,
    headers,
    body: body === undefined ? undefined : JSON.stringify(body),
    signal
  })
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.detail || `Erro HTTP ${response.status}`)
  }
  if (response.status === 204) return null
  return response.json()
}
