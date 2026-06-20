import { useEffect, useState } from "react";

const styles = {
  body: {
    fontFamily: "'Inter', sans-serif",
    background: "#0a0a0a",
    color: "#e8e8e8",
    minHeight: "100vh",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    padding: "2rem",
    margin: 0,
  },
  container: {
    maxWidth: "520px",
    width: "100%",
    textAlign: "center",
  },
  badge: {
    display: "inline-flex",
    alignItems: "center",
    gap: "8px",
    background: "#1a1a1a",
    border: "1px solid #2a2a2a",
    borderRadius: "100px",
    padding: "6px 16px",
    fontSize: "12px",
    color: "#888",
    letterSpacing: "0.08em",
    textTransform: "uppercase",
    marginBottom: "2.5rem",
  },
  dot: {
    width: "6px",
    height: "6px",
    borderRadius: "50%",
    background: "#f59e0b",
  },
  h1: {
    fontSize: "clamp(1.8rem, 5vw, 2.6rem)",
    fontWeight: 600,
    color: "#ffffff",
    lineHeight: 1.2,
    marginBottom: "1rem",
    letterSpacing: "-0.03em",
  },
  h1span: {
    color: "#888",
  },
  p: {
    fontSize: "1rem",
    color: "#666",
    lineHeight: 1.7,
    marginBottom: "2.5rem",
  },
  divider: {
    width: "40px",
    height: "1px",
    background: "#2a2a2a",
    margin: "0 auto 2.5rem",
  },
  newApp: {
    background: "#111",
    border: "1px solid #2a2a2a",
    borderRadius: "16px",
    padding: "1.5rem",
    marginBottom: "2rem",
  },
  newAppLabel: {
    fontSize: "11px",
    textTransform: "uppercase",
    letterSpacing: "0.1em",
    color: "#555",
    marginBottom: "0.75rem",
  },
  newAppName: {
    fontSize: "1.25rem",
    fontWeight: 600,
    color: "#ffffff",
    marginBottom: "0.5rem",
  },
  newAppUrl: {
    fontSize: "13px",
    color: "#555",
    fontFamily: "monospace",
    marginBottom: "1.25rem",
  },
  btn: {
    display: "inline-flex",
    alignItems: "center",
    gap: "8px",
    background: "#ffffff",
    color: "#0a0a0a",
    fontFamily: "'Inter', sans-serif",
    fontSize: "14px",
    fontWeight: 500,
    padding: "10px 24px",
    borderRadius: "100px",
    border: "none",
    cursor: "pointer",
    textDecoration: "none",
    transition: "opacity 0.15s",
  },
  countdown: {
    fontSize: "13px",
    color: "#444",
    marginBottom: "1rem",
  },
  countdownSpan: {
    color: "#666",
    fontWeight: 500,
  },
  author: {
    marginTop: "3rem",
    fontSize: "12px",
    color: "#333",
  },
  authorStrong: {
    color: "#444",
  },
};

export default function App() {
  const [count, setCount] = useState(8);

  useEffect(() => {
    const interval = setInterval(() => {
      setCount((c) => {
        if (c <= 1) {
          clearInterval(interval);
          window.location.href = "https://nova-wallet-mu.vercel.app/";
          return 0;
        }
        return c - 1;
      });
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div style={styles.body}>
      <div style={styles.container}>

        <div style={styles.badge}>
          <div style={styles.dot} />
          Nuevo proyecto disponible
        </div>

        <h1 style={styles.h1}>
          Algo nuevo <span style={styles.h1span}>por acá.</span>
        </h1>

        <p style={styles.p}>
          Esta app ya no está activa. Chequeá mi nuevo proyecto.
        </p>

        <div style={styles.divider} />

        <div style={styles.newApp}>
          <div style={styles.newAppLabel}>Nueva aplicación</div>
          <div style={styles.newAppName}>Nova Wallet</div>
          <div style={styles.newAppUrl}>nova-wallet-mu.vercel.app</div>
          <a
            style={styles.btn}
            href="https://nova-wallet-mu.vercel.app/"
          >
            Ir a Nova Wallet
            <svg width="14" height="14" viewBox="0 0 16 16" fill="none">
              <path d="M3 8h10M9 4l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </a>
        </div>

        <div style={styles.countdown}>
          Redirigiendo en <span style={styles.countdownSpan}>{count}</span> segundos...
        </div>

        <div style={styles.author}>
          Hecho por <strong style={styles.authorStrong}>Mateo Calcagno</strong>
        </div>

      </div>
    </div>
  );
}