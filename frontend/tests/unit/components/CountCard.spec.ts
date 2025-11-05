import { mount } from '@vue/test-utils'
import CountCard from '../../../src/components/CountCard.vue'

describe('CountCard.vue', () => {
  it('renders numLen and valName props correctly', () => {
    const wrapper = mount(CountCard, {
      props: {
        numLen: 42,
        valName: 'Batches',
      },
    })
    expect(wrapper.find('.num-background').text()).toBe('42')
    expect(wrapper.find('.val-name').text()).toBe('Batches')
    expect(wrapper.html()).toMatchSnapshot()
  })
})
