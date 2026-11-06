/**
 * ReWear - Sustainable Fashion Platform
 * Core Frontend Interactions & Dynamic State Management
 */

document.addEventListener('DOMContentLoaded', () => {
  initToasts();
  initTabs();
  initModals();
  initEcoCalculator();
  initImagePreview();
  initListingFilters();
});

/* ========================================================
   1. Toast Notifications
   ======================================================== */
function initToasts() {
  const toasts = document.querySelectorAll('.toast');
  toasts.forEach(toast => {
    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateX(100%)';
      toast.style.transition = 'all 0.4s ease';
      setTimeout(() => toast.remove(), 400);
    }, 4500);
  });
}

function showToast(message, type = 'success') {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `
    <span>${type === 'success' ? '🌱' : '⚠️'}</span>
    <div style="flex-grow: 1; font-size: 0.9rem;">${message}</div>
    <button style="background:none; border:none; cursor:pointer; font-size:1.1rem; color:#94A3B8;" onclick="this.parentElement.remove()">✕</button>
  `;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.4s ease';
    setTimeout(() => toast.remove(), 400);
  }, 4000);
}

/* ========================================================
   2. Tabbed Navigation
   ======================================================== */
function initTabs() {
  const tabContainers = document.querySelectorAll('[data-tabs]');
  tabContainers.forEach(container => {
    const tabButtons = container.querySelectorAll('.tab-btn');
    const tabPanes = document.querySelectorAll('.tab-pane');

    tabButtons.forEach(btn => {
      btn.addEventListener('click', () => {
        const targetId = btn.getAttribute('data-target');
        
        tabButtons.forEach(b => b.classList.remove('active'));
        tabPanes.forEach(p => p.classList.remove('active'));

        btn.classList.add('active');
        const activePane = document.getElementById(targetId);
        if (activePane) activePane.classList.add('active');
      });
    });
  });
}

/* ========================================================
   3. Modal Controllers
   ======================================================== */
function initModals() {
  // Modal triggers
  document.querySelectorAll('[data-open-modal]').forEach(trigger => {
    trigger.addEventListener('click', (e) => {
      e.preventDefault();
      const modalId = trigger.getAttribute('data-open-modal');
      const modal = document.getElementById(modalId);
      if (modal) {
        modal.classList.add('open');
        document.body.style.overflow = 'hidden';
      }
    });
  });

  // Close triggers
  document.querySelectorAll('[data-close-modal], .modal-backdrop').forEach(closer => {
    closer.addEventListener('click', (e) => {
      if (e.target === closer || closer.hasAttribute('data-close-modal')) {
        const modal = closer.closest('.modal-backdrop');
        if (modal) {
          modal.classList.remove('open');
          document.body.style.overflow = '';
        }
      }
    });
  });

  // ESC key closes any open modal
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      document.querySelectorAll('.modal-backdrop.open').forEach(modal => {
        modal.classList.remove('open');
      });
      document.body.style.overflow = '';
    }
  });
}

function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.add('open');
    document.body.style.overflow = 'hidden';
  }
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.remove('open');
    document.body.style.overflow = '';
  }
}

/* ========================================================
   4. Sustainability Calculator
   ======================================================== */
function initEcoCalculator() {
  const slider = document.getElementById('garments-slider');
  if (!slider) return;

  const garmentCountDisplay = document.getElementById('calc-garments-count');
  const textileDisplay = document.getElementById('calc-textile');
  const waterDisplay = document.getElementById('calc-water');
  const co2Display = document.getElementById('calc-co2');

  function updateCalculator() {
    const count = parseInt(slider.value, 10);
    if (garmentCountDisplay) garmentCountDisplay.textContent = count;

    // Formulas grounded in UNEP & Ellen MacArthur Foundation fashion lifecycle data:
    // ~0.8 kg textile landfill diversion per piece
    // ~2,700 L water saved per garment (avg cotton shirt / denim)
    // ~10.0 kg CO2 emissions prevented
    const textileKg = (count * 0.8).toFixed(1);
    const waterLiters = (count * 2700).toLocaleString();
    const co2Kg = (count * 10.0).toFixed(0);

    if (textileDisplay) textileDisplay.textContent = `${textileKg} kg`;
    if (waterDisplay) waterDisplay.textContent = `${waterLiters} L`;
    if (co2Display) co2Display.textContent = `${co2Kg} kg`;
  }

  slider.addEventListener('input', updateCalculator);
  updateCalculator();
}

/* ========================================================
   5. Image File Preview
   ======================================================== */
function initImagePreview() {
  const imageInput = document.getElementById('image-upload');
  const previewImg = document.getElementById('image-preview');
  const placeholder = document.getElementById('upload-placeholder');

  if (!imageInput || !previewImg) return;

  imageInput.addEventListener('change', () => {
    const file = imageInput.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        previewImg.src = e.target.result;
        previewImg.style.display = 'block';
        if (placeholder) placeholder.style.display = 'none';
      };
      reader.readAsDataURL(file);
    }
  });
}

/* ========================================================
   6. Dynamic Search & Filter on Listings Page
   ======================================================== */
function initListingFilters() {
  const filterForm = document.getElementById('listings-filter-form');
  const listingsGrid = document.getElementById('listings-container');
  const countIndicator = document.getElementById('results-count');

  if (!filterForm || !listingsGrid) return;

  const searchInput = filterForm.querySelector('input[name="q"]');
  const categorySelect = filterForm.querySelector('select[name="category"]');
  const sizeSelect = filterForm.querySelector('select[name="size"]');
  const conditionSelect = filterForm.querySelector('select[name="condition"]');
  const typeSelect = filterForm.querySelector('select[name="type"]');
  const statusSelect = filterForm.querySelector('select[name="status"]');

  function filterItems() {
    const q = (searchInput?.value || '').toLowerCase().trim();
    const cat = categorySelect?.value || 'all';
    const size = sizeSelect?.value || 'all';
    const cond = conditionSelect?.value || 'all';
    const type = typeSelect?.value || 'all';
    const status = statusSelect?.value || 'all';

    const cards = listingsGrid.querySelectorAll('.listing-card');
    let visibleCount = 0;

    cards.forEach(card => {
      const title = (card.getAttribute('data-title') || '').toLowerCase();
      const desc = (card.getAttribute('data-desc') || '').toLowerCase();
      const brand = (card.getAttribute('data-brand') || '').toLowerCase();
      const cardCat = card.getAttribute('data-category') || '';
      const cardSize = card.getAttribute('data-size') || '';
      const cardCond = card.getAttribute('data-condition') || '';
      const cardType = card.getAttribute('data-type') || '';
      const cardStatus = card.getAttribute('data-status') || '';

      const matchQ = !q || title.includes(q) || desc.includes(q) || brand.includes(q);
      const matchCat = cat === 'all' || cardCat === cat;
      const matchSize = size === 'all' || cardSize === size;
      const matchCond = cond === 'all' || cardCond === cond;
      const matchType = type === 'all' || cardType === type || cardType === 'Both';
      const matchStatus = status === 'all' || cardStatus === status;

      if (matchQ && matchCat && matchSize && matchCond && matchType && matchStatus) {
        card.style.display = 'flex';
        visibleCount++;
      } else {
        card.style.display = 'none';
      }
    });

    if (countIndicator) {
      countIndicator.textContent = `${visibleCount} item${visibleCount === 1 ? '' : 's'} found`;
    }

    const emptyState = document.getElementById('empty-listings');
    if (emptyState) {
      emptyState.style.display = visibleCount === 0 ? 'block' : 'none';
    }
  }

  // Bind event listeners for instant responsiveness
  if (searchInput) searchInput.addEventListener('input', filterItems);
  if (categorySelect) categorySelect.addEventListener('change', filterItems);
  if (sizeSelect) sizeSelect.addEventListener('change', filterItems);
  if (conditionSelect) conditionSelect.addEventListener('change', filterItems);
  if (typeSelect) typeSelect.addEventListener('change', filterItems);
  if (statusSelect) statusSelect.addEventListener('change', filterItems);

  // Filter category chips if clicked
  document.querySelectorAll('.cat-chip-btn').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('.cat-chip-btn').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      if (categorySelect) {
        categorySelect.value = chip.getAttribute('data-cat');
        filterItems();
      }
    });
  });
}
