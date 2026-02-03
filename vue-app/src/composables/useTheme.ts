import { ref, watch } from 'vue'

export type Theme = 'light' | 'dark'

const THEME_KEY = 'baby-app-theme'

const currentTheme = ref<Theme>((localStorage.getItem(THEME_KEY) as Theme) || 'light')

export function useTheme() {
  const isDark = ref(currentTheme.value === 'dark')

  function setTheme(theme: Theme) {
    currentTheme.value = theme
    isDark.value = theme === 'dark'
    localStorage.setItem(THEME_KEY, theme)
    applyTheme(theme)
  }

  function toggleTheme() {
    const newTheme: Theme = isDark.value ? 'light' : 'dark'
    setTheme(newTheme)
  }

  function applyTheme(theme: Theme) {
    if (theme === 'dark') {
      document.documentElement.setAttribute('data-theme', 'dark')
    } else {
      document.documentElement.removeAttribute('data-theme')
    }
  }

  // Initialize theme on load
  function initTheme() {
    applyTheme(currentTheme.value)
  }

  // Watch for system theme changes
  function watchSystemTheme() {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')

    const handleChange = (e: MediaQueryListEvent) => {
      // Only auto-switch if user hasn't manually set a preference
      if (!localStorage.getItem(THEME_KEY)) {
        setTheme(e.matches ? 'dark' : 'light')
      }
    }

    mediaQuery.addEventListener('change', handleChange)
    return () => mediaQuery.removeEventListener('change', handleChange)
  }

  return {
    theme: currentTheme,
    isDark,
    setTheme,
    toggleTheme,
    initTheme,
    watchSystemTheme,
  }
}
